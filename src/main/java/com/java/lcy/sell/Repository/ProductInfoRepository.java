package com.java.lcy.sell.Repository;

import com.java.lcy.sell.Entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    List<ProductInfo>  findByProductStatus(Integer productStatus);
}
