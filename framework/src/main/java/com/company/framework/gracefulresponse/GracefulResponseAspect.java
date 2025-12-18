package com.company.framework.gracefulresponse;

import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GracefulResponse切面
 *
 * @author JQ棣
 */
@Slf4j
@Aspect
@Component
public class GracefulResponseAspect {

    @Autowired
    private GracefulResponseProperties gracefulResponseProperties;

    @AfterReturning(pointcut = "@within(org.springframework.cloud.openfeign.FeignClient)", returning = "result")
    public void afterAnyFeignCall(Object result) {
        if (result != null) {
            return;
        }
        GracefulResponseException gracefulResponseException = GracefulResponseExceptionContext.getException();
        GracefulResponseExceptionContext.removeException();
        if (gracefulResponseException == null) {
            return;
        }
        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
        if (!defaultSuccessCode.equals(gracefulResponseException.getCode())) {
            throw gracefulResponseException;
        }
    }
}
