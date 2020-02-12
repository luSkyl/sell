package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Converter.OrderMaster2OrderDTOConverter;
import com.java.lcy.sell.Dto.CartDto;
import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Entity.OrderDetail;
import com.java.lcy.sell.Entity.OrderMaster;
import com.java.lcy.sell.Entity.ProductInfo;
import com.java.lcy.sell.Enums.OrderStatusEnum;
import com.java.lcy.sell.Enums.PayStatusEnum;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Repository.OrderDetailRepository;
import com.java.lcy.sell.Repository.OrderMasterRepository;
import com.java.lcy.sell.Service.*;
import com.java.lcy.sell.Utils.keyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private PayService payService;
    @Autowired
    private PushMessage pushMessage;
    @Autowired
    private WebSocket webSocket;

    BigDecimal orderAccount = new BigDecimal(BigInteger.ZERO);

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = keyUtil.UniqueKey();
        for (OrderDetail orderDetail : orderDto.getOrderDetailList()) {
            ProductInfo productInfo = productInfoService.findById(orderDetail.getProductId());
            if (null == productInfo) {
                log.error("【创建订单】 service  商品不存在 orderDto={}", orderDto);
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            orderAccount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAccount);
            //订单详情入库
            orderDetail.setDetailId(keyUtil.UniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //买家信息入库
        orderDto.setOrderId(orderId);
        OrderMaster orderMaster = OrderMaster2OrderDTOConverter.masterConvert(orderDto);
        orderMaster.setOrderAmount(orderAccount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //扣减库存
        List<CartDto> cartDtoList =
                orderDto.getOrderDetailList().stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.decreaseStock(cartDtoList);

        //发送websocket消息
        webSocket.sendMessage(orderDto.getOrderId());

        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (null == orderMaster) {
            log.error("【查找订单】 买家不存在 orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_MASTER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.error("【查找订单】  订单详情不存在  orderId={}, orderDetailList={}", orderId, orderDetailList);
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDto orderDto = OrderMaster2OrderDTOConverter.convert(orderMaster);
        orderDto.setOrderDetailList(orderDetailList);

        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDto> orderDtoList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDtoList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确 ，orderId={} ，orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster orderMaster = OrderMaster2OrderDTOConverter.masterConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (null == updateResult) {
            log.error("【取消订单】 更新失败 , orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】 订单中无商品详情 , orderDto={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDto> cartDtoList = orderDTO.getOrderDetailList().stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.increaseStock(cartDtoList);

        //如果已支付需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS)) {
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    @Override
    public OrderDto finish(OrderDto orderDTO) {

        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单完结】 订单状态不正确 ，orderId={} ，orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = OrderMaster2OrderDTOConverter.masterConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (null == updateResult) {
            log.error("【订单完结】 更新失败 , orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }

        //微信推送消息
        pushMessage.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】 订单状态不正确 ，orderId={} ，orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】 订单支付状态不正确 ，orderId={}", orderDTO.getOrderId());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = OrderMaster2OrderDTOConverter.masterConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (null == updateResult) {
            log.error("【订单支付完成】 更新失败 , orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDto> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);

        List<OrderDto> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
