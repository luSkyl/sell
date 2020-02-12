package com.java.lcy.sell.Repository;

import com.java.lcy.sell.Entity.ProductCategory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(1);
        System.out.println(productCategory.get().toString());
    }

    @Test
    public void save(){
        ProductCategory productCategory = new ProductCategory("男生最爱",3);
        ProductCategory result = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(result);

    }
    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> categoryLists = Arrays.asList(2, 3);
        List<ProductCategory> categories = productCategoryRepository.findByCategoryTypeIn(categoryLists);
        Assert.assertNotNull(categories);
    }

}