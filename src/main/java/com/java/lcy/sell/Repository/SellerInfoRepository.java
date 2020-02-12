package com.java.lcy.sell.Repository;

import com.java.lcy.sell.Entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo,String> {
    SellerInfo findByOpenid(String openId);
}
