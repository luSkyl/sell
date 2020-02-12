package com.java.lcy.sell.Enums;

import com.java.lcy.sell.Service.CodeEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum  OrderStatusEnum implements CodeEnum {
    NEW(0,"新订单"),
    FINISHED(1,"完结"),
    CANCEL(2,"取消");

    private Integer code;
    private  String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
