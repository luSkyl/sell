package com.java.lcy.sell.Repository;

import com.java.lcy.sell.Entity.OrderMaster;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;
    private static final String OPENID = "19801208450";

    @Test
    public void save(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123456");
        orderMaster.setBuyerName("lcy");
        orderMaster.setBuyerPhone("19801208450");
        orderMaster.setBuyerAddress("防灾科技学院北区六号楼340");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(22.0));

        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(result);

    }

    @Test
    void findByBuyerOpenid() {
        PageRequest page = PageRequest.of(0,2);
        Page<OrderMaster> ordermaster = orderMasterRepository.findByBuyerOpenid(OPENID, page);
        System.out.println(ordermaster.getTotalElements());
    }
}