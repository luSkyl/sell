package com.java.lcy.sell.Utils;

import java.util.Random;

public class keyUtil {

    /**
     * 生成唯一主键
     * @return
     */
    public  static synchronized String UniqueKey(){
        Random random = new Random();
        Integer number =  random.nextInt(900000)+100000;
        return  System.currentTimeMillis()+String.valueOf(number);
    }
}
