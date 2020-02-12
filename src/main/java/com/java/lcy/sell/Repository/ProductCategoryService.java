package com.java.lcy.sell.Repository;


import com.java.lcy.sell.Entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory findById(Integer id);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryType);

    ProductCategory save(ProductCategory productCategory);

}
