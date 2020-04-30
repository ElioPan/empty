package com.ev.framework.exception;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.common.domain.LogDO;
import com.ev.common.service.LogService;
import com.ev.framework.utils.HttpServletUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.UserDO;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    LogService logService;

    /**
     * 自定义异常
     */
    @ExceptionHandler(BDException.class)
    public R handleBDException(BDException e) {
        logger.error(e.getMessage(), e);
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    @ExceptionHandler(WorkWxException.class)
    public R handleGlobalExceptio(WorkWxException e) {
        logger.error(e.getMsg(), e);
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(ACException.class)
    public R handlerACException(ACException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        return R.error(98, "用户令牌不正确或者已过期，请重新登录！");
    }

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return R.error(403, "未授权");
        }
        return new ModelAndView("error/403");
    }


    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e, HttpServletRequest request) {
        LogDO logDO = new LogDO();
        logDO.setCreateTime(new Date());
        logDO.setOperation(Constant.LOG_ERROR);
        logDO.setMethod(request.getRequestURL().toString());
        logDO.setParams(e.toString());
        UserDO current = ShiroUtils.getUser();
        if(null!=current){
            logDO.setUserId(current.getUserId());
            logDO.setUsername(current.getUsername());
        }
        logService.save(logDO);
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return R.error(500, "服务器错误，请联系管理员");
        }
        return new ModelAndView("error/500");
    }
}
