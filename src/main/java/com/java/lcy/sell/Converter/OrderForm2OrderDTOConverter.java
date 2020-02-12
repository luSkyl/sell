package com.java.lcy.sell.Converter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Entity.OrderDetail;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**

 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDto convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDto orderDto = new OrderDto();

        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);

        return orderDto;
    }
}
