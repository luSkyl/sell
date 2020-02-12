package com.java.lcy.sell.Service;


import com.java.lcy.sell.Dto.OrderDto;

public interface BuyerService {

    /**
     * 查询一个订单
     * @param openId
     * @param orderId
     * @return
     */
    OrderDto findOrderOne(String openId,String orderId);

    /**
     * 取消订单
     * @param openId
     * @param orderId
     * @return
     */
    OrderDto cancelOrder(String openId,String orderId);


}
