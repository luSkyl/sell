package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Service.RedisLock;
import com.java.lcy.sell.Service.SecKillService;
import com.java.lcy.sell.Utils.keyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    private static final long TIMEOUT = 10 * 1000;
    @Autowired
    private RedisLock redisLock;

    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String>orders;
    static {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("124578",10000);
        stock.put("124578",10000);
    }

    private String queryMap(String productId){
        return "国庆节，皮蛋粥特价，限量份"
                +products.get(productId)
                +"还剩："+stock.get(productId)+"份"
                +"该商品成功下单用户数量："
                +orders.size()+"人";
    }


    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {
        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(productId,String.valueOf(time))){
            throw new SellException(101,"哎哟喂,人也太多了，换个姿势试试~~~");
        }

        Integer stockNum = SecKillServiceImpl.stock.get(productId);
        if(stockNum == 0 ){
            throw new SellException(100,"活动结束");
        }else {
            orders.put(keyUtil.UniqueKey(),productId);
            stockNum -=1;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stock.put(productId,stockNum);

         redisLock.unlock(productId,String.valueOf(time));
    }
}
