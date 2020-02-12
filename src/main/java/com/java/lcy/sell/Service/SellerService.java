package com.java.lcy.sell.Service;

import com.java.lcy.sell.Entity.SellerInfo;

public interface SellerService {

    /**
     * 通过openId查询卖家端信息
     * @param openId
     * @return
     */
    SellerInfo findSellerInfoByOpenId(String openId);
}
