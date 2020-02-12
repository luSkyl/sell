package com.java.lcy.sell.Converter;


import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Entity.OrderMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * OrderMaster转换成OrderDTO
 *
 */
public class OrderMaster2OrderDTOConverter {

    public static OrderDto convert(OrderMaster orderMaster) {

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        return orderDto;
    }

    public static OrderMaster masterConvert(OrderDto OrderDto) {

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(OrderDto, orderMaster);
        return orderMaster;
    }


    public static List<OrderDto> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }


}
