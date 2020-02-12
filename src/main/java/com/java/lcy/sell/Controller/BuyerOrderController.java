package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Converter.OrderForm2OrderDTOConverter;
import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Form.OrderForm;
import com.java.lcy.sell.Service.BuyerService;
import com.java.lcy.sell.Service.OrderService;
import com.java.lcy.sell.Utils.ResultVoUtil;
import com.java.lcy.sell.Vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVo<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】 参数不正确 ， orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDto orderDto = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            log.error("【创建订单】 购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDto createResult = orderService.create(orderDto);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVoUtil.success(map);
    }


    //订单列表
    @GetMapping("/list")
    public ResultVo<List<OrderDto>> list(@RequestParam("openId") String openId,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openId)) {
            log.error("【查询订单列表】 openId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<OrderDto> pageList = orderService.findList(openId, pageable);
        return ResultVoUtil.success(pageList.getContent());
    }

    @GetMapping("/detail")
    public ResultVo<OrderDto> detail(@RequestParam("openId") String openId,
                                     @RequestParam("orderId") String orderId) {

        OrderDto orderDto = null;
        try {
            orderDto = buyerService.findOrderOne(openId,orderId);
        } catch (SellException e) {
            log.error("【买家订单详情】 查找订单详情错误");
            e.printStackTrace();
        }
        return ResultVoUtil.success(orderDto);
    }

    @PostMapping("/cancel")
    public ResultVo cancel(@RequestParam("openId") String openId,
                           @RequestParam("orderId") String orderId) {

        buyerService.cancelOrder(openId, orderId);
        return ResultVoUtil.success();
    }


}
