package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerService implements com.java.lcy.sell.Service.BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDto findOrderOne(String openId, String orderId) {
        return checkOrderOwner(openId,orderId);
    }

    @Override
    public OrderDto cancelOrder(String openId, String orderId) {
        OrderDto orderDto = checkOrderOwner(openId, orderId);
        if(null == orderDto){
            log.error("【取消订单】 查询不到订单, openId={} , orderId={}",openId,orderId);
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        return orderService.cancel(orderDto);
    }

    /**
     * 检查是否属于自己的订单
     * @param openId
     * @param orderId
     * @return
     */
    private OrderDto checkOrderOwner(String openId, String orderId){
        OrderDto orderDto = orderService.findOne(orderId);
        if(null == orderDto) return null;
        if(orderDto.getBuyerOpenid().equalsIgnoreCase(openId)){
            log.error("【查询订单】 订单的openId不一致 , openId={}, orderId={}",openId,orderId);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDto;
    }

}
