package com.java.lcy.sell.Service;

import com.java.lcy.sell.Dto.OrderDto;

/**
 * 推送消息
 */
public interface PushMessage {

    /**
     * 订单状态变更消息
     * @param orderDto
     */
    void orderStatus(OrderDto orderDto);
}
