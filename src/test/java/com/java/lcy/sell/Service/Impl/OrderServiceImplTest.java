package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Dto.OrderDto;
import com.java.lcy.sell.Entity.OrderDetail;
import com.java.lcy.sell.Enums.OrderStatusEnum;
import com.java.lcy.sell.Enums.PayStatusEnum;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private static final String OPEN_ID = "123456789";
    private static final String OPEN_ID2 = "19801208450";

    @Test
    void create() {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName("lcy");
        orderDto.setBuyerAddress("防灾科技学院北区六号楼340");
        orderDto.setBuyerPhone("19801208450");
        orderDto.setBuyerOpenid(OPEN_ID2);

        /**/
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123459");
        o1.setProductQuantity(1);
        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123458");
        o2.setProductQuantity(1);
        orderDetails.add(o2);
        orderDetails.add(o1);
        orderDto.setOrderDetailList(orderDetails);

        OrderDto result = orderService.create(orderDto);
        System.out.println(result);

    }

    @Test
    void findOne() {
        OrderDto one = orderService.findOne("1580300342318370305");
        Assert.assertNotNull(one);
    }

    @Test
    void findList() {
        PageRequest page = PageRequest.of(0,2);
        Page<OrderDto> orderDtolist = orderService.findList(OPEN_ID, page);
        Assert.assertNotNull(orderDtolist);
    }

    @Test
    void cancel() {
        OrderDto one = orderService.findOne("1580351638938883084");
        OrderDto orderDto = orderService.cancel(one);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),orderDto.getOrderStatus());
    }

    @Test
    void finish() {

    }

    @Test
    void paid() {
        OrderDto one = orderService.findOne("1580351638938883084");
        OrderDto orderDto = orderService.paid(one);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),orderDto.getPayStatus());
    }

    @Test
    void findList1() {
        PageRequest page = PageRequest.of(0,2);
        Page<OrderDto> orderDtoList = orderService.findList(page);
        Assert.assertNotNull(orderDtoList);
    }
}