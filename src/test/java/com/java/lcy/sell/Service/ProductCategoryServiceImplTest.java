package com.java.lcy.sell.Service;

import com.java.lcy.sell.Entity.ProductCategory;
import com.java.lcy.sell.Service.Impl.ProductCategoryServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryServiceImpl productCategoryService;



    @Test
    void findById() {
        ProductCategory productCategory = productCategoryService.findById(1);
        Assert.assertEquals(new Integer(1),productCategory.getCategoryId());

    }

    @Test
    void findAll() {
    }

    @Test
    void findByCategoryTypeIn() {
    }

    @Test
    void save() {
    }
}