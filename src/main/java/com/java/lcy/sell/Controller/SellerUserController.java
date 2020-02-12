package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Config.ProjectUrlConfig;
import com.java.lcy.sell.Constant.CookieConstant;
import com.java.lcy.sell.Constant.RedisConstatnt;
import com.java.lcy.sell.Entity.SellerInfo;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Service.SellerService;
import com.java.lcy.sell.Utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
@Slf4j
public class SellerUserController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    private ModelAndView login(@RequestParam("openId") String openId,
                               HttpServletResponse response,
                               Map<String, Object> map) {
        //1、 openId去和数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenId(openId);
        if (null == sellerInfo) {
            log.error("【微信登入】 登入失败 卖家信息不存在 openId={}", openId);
            map.put("msg", ResultEnum.SELLER_INFO_NOT_EXIST.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        //2、设备token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstatnt.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstatnt.TOKEN_PREFIX, token), openId, expire, TimeUnit.SECONDS);

        //3、设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");
    }

    @GetMapping("/logout")
    private ModelAndView logout(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> map) {
        //从cookie中查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (null != cookie) {
            //清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstatnt.TOKEN_PREFIX, cookie.getValue()));
            //清除cookie
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);

    }
}
