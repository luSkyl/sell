package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Entity.SellerInfo;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Repository.SellerInfoRepository;
import com.java.lcy.sell.Service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenId(String openId) {
        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid(openId);
        if(null == sellerInfo){
            log.error("【根据openId查询卖家信息】 根据openId查询不到卖家信息 openId={}",openId);
            throw new SellException(ResultEnum.SELLER_INFO_NOT_EXIST);
        }
        return sellerInfo;
    }
}
