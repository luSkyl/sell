package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Service.OrderService;
import com.java.lcy.sell.Service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        OrderDto orderDto = orderService.findOne(orderId);
        if (null == orderDto) {
            log.error("【微信支付】 orderDto为空 orderId={},returnUrl={}", orderId, returnUrl);
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        //发起支付
        PayResponse payResponse = payService.create(orderDto);
        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }

    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);
        return new ModelAndView("pay/success");
    }

}
