package com.java.lcy.sell.Enums;

import com.java.lcy.sell.Service.CodeEnum;
import lombok.Getter;

@Getter
public enum  ProductStatusEnum implements CodeEnum {
    UP(0,"上架"),
    DOWN(1,"下架");
    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
