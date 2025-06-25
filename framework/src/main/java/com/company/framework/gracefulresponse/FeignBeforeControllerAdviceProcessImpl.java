package com.company.framework.gracefulresponse;

import com.company.framework.gracefulresponse.exception.BusinessFeignException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.advice.lifecycle.exception.BeforeControllerAdviceProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认的处理前回调
 *
 * @author qinyujie
 */
//@Component
public class FeignBeforeControllerAdviceProcessImpl implements BeforeControllerAdviceProcess {
    private final Logger logger = LoggerFactory.getLogger(FeignBeforeControllerAdviceProcessImpl.class);

    @Resource
    private GracefulResponseProperties properties;

    @Override
    public void call(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {

        Throwable cex = ex;
        Throwable cause = ex.getCause();
        while (cause != null && !(cause instanceof BusinessFeignException)) {
//            if (cause instanceof BusinessFeignException) {
//                cex = cause;
//                break;
//            }
            cex = cause;
            cause = cause.getCause();
        }
        ex = (Exception) cex;

        if (properties.isPrintExceptionInGlobalAdvice()) {
            logger.error("Graceful Response:捕获到异常,message=[{}]", ex.getMessage(), ex);
        }
    }
}
