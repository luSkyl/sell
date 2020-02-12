package com.java.lcy.sell.Aspect;

import com.java.lcy.sell.Constant.RedisConstatnt;
import com.java.lcy.sell.Exception.SellerAuthorizeException;
import com.java.lcy.sell.Constant.CookieConstant;
import com.java.lcy.sell.Utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

   // @Pointcut("execution(public * com.java.lcy.sell.Controller.Sell*.*(..) )"+"&& !execution(public * com.java.lcy.sell.Controller.SellerUserController.*(..))")
    public void verify(){};

    //@Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(null == cookie){
            log.warn("【登录校验】 cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstatnt.TOKEN_PREFIX,cookie));
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登录校验】 Redis中查不到Token");
            throw new SellerAuthorizeException();
        }

    }
}
