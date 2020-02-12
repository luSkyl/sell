package com.java.lcy.sell.Dto;

import lombok.Data;

/**
 * 购物车
 */
@Data
public class CartDto {

    /*商品Id*/
    private String productId;

    /*商品数量*/
    private Integer productQuantity;

    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
