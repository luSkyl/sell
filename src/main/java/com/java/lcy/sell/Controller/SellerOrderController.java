package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Service.OrderService;
import com.java.lcy.sell.Service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.plaf.PanelUI;
import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             Map<String,Object> map){
        PageRequest pageRequest = PageRequest.of(page-1,size);
        Page<OrderDto> orderDtoPage = orderService.findList(pageRequest);
        map.put("orderDTOPage",orderDtoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return  new ModelAndView("order/list",map);
    }

    @RequestMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId")String orderId,
                               Map<String,Object> map){
        OrderDto orderDto = null;
        try {
            orderDto = orderService.findOne(orderId);
            orderService.cancel(orderDto);
        } catch (SellException e) {
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");
            log.error("【卖家端取消订单】 查询不到订单");
            return new ModelAndView("common/error",map);
        }
        map.put("msg",ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }

    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId")String orderId,
                               Map<String,Object> map){
        OrderDto orderDto = null;
        try {
            orderDto = orderService.findOne(orderId);
        } catch (SellException e) {
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");
            log.error("【卖家端查询订单详情】 查询不到订单");
            return new ModelAndView("common/error",map);
        }
        map.put("orderDTO",orderDto);
        return new ModelAndView("order/detail",map);
    }

    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId")String orderId,
                               Map<String,Object> map){
        OrderDto orderDto = null;
        try {
            orderDto = orderService.findOne(orderId);
            orderService.finish(orderDto);
        } catch (SellException e) {
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");
            log.error("【卖家端完结订单】 查询不到订单");
            return new ModelAndView("common/error",map);
        }
        map.put("msg",ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }


}
