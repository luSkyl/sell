package com.java.lcy.sell.Repository;

import com.java.lcy.sell.Entity.ProductInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    public void save(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("孜然羊肉");
        productInfo.setProductPrice(new BigDecimal(10.0));
        productInfo.setProductStock(10);
        productInfo.setProductDescription("很好吃 很值得推荐的一道菜");
        productInfo.setProductIcon("http://xxxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(1);

        ProductInfo pro = productInfoRepository.save(productInfo);
        System.out.println(pro.toString());

    }

    @Test
    void findByProductStatus() {
        List<ProductInfo> productInfo = productInfoRepository.findByProductStatus(0);
        System.out.println(productInfo.size());

    }
}