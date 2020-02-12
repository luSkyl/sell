package com.java.lcy.sell.Vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * http返回的最外层对象
 */
@Data
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = -4728979916733492258L;
    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体内容.
     */
    private T data;

}
