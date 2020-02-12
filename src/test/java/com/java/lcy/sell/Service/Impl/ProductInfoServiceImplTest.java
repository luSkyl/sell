package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Entity.ProductInfo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl productInfoService;

    @Test
    void findById() {
        ProductInfo product = productInfoService.findById("123456");
        Assert.assertNotNull(product);
    }

    @Test
    void findUpAll() {
        List<ProductInfo> productInfos = productInfoService.findUpAll();
        Assert.assertNotNull(productInfos);
    }

    @Test
    void findAll() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<ProductInfo> productInfos = productInfoService.findAll(pageRequest);
        System.out.println(productInfos.getTotalElements());
    }

    @Test
    void save() {
    }
}