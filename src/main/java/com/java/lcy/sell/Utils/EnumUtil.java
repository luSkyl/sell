package com.java.lcy.sell.Utils;

import com.java.lcy.sell.Service.CodeEnum;

public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for (T enumConstant : enumClass.getEnumConstants()) {
            if(code.equals(enumConstant.getCode())){
                return enumConstant;
            }
        }
        return null;
    }
}
