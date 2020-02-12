package com.java.lcy.sell.Handler;

import com.java.lcy.sell.Config.ProjectUrlConfig;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Exception.SellerAuthorizeException;
import com.java.lcy.sell.Utils.ResultVoUtil;
import com.java.lcy.sell.Vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice//全局异常处理 全局数据绑定 全局数据预处理
public class SellExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?returnUrl=")
                .concat(projectUrlConfig.getSell())
                .concat("/sell/seller/login"));
    }

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVo handlerSellerException(SellException e){
        return ResultVoUtil.error(e.getCode(),e.getMessage());
    }
}
