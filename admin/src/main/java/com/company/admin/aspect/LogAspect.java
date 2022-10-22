package com.company.admin.aspect;

import java.lang.reflect.Parameter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.admin.annotation.Log;
import com.company.common.api.Result;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;

/**
 * @author xxw
 * @date 2018/9/26
 */
@Component
@Aspect
@Order(100)
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired(required = false)
    private List<Logable> logables;

    @Around("@annotation(log)")
    public Object around(JoinPoint joinPoint, Log log) throws Throwable {
        HttpServletRequest request = HttpContextUtil.request();
        Object param;
        Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        int i = 0;
        for (; i < parameters.length; ++i) {
            boolean isRequestBody = parameters[i].isAnnotationPresent(RequestBody.class);
            if (isRequestBody) {
                break;
            }
        }
        if (i < parameters.length) {
            param = joinPoint.getArgs()[i];
        }
        else {
            param = request.getParameterMap();
        }
        if (logger.isDebugEnabled()) {
            StringBuilder consoleLog = new StringBuilder()
                    .append(StringUtils.LF)
                    .append("请求地址：")
                    .append(StringUtils.SPACE)
                    .append(request.getRequestURI())
                    .append(StringUtils.LF)
                    .append("请求参数：")
                    .append(StringUtils.LF)
                    .append(JsonUtil.toJsonString(param));
            logger.debug(consoleLog.toString());
        }
        Object result = StringUtils.EMPTY;
        boolean success = false;
        try {
            result = ((ProceedingJoinPoint) joinPoint).proceed(joinPoint.getArgs());
            success = true;
            return result;
        } catch (Throwable t) {
            if (t instanceof BusinessException) {
            	BusinessException e = (BusinessException) t;
				result = Result.fail(e);
			}
            else {
                result = Result.fail(t.getMessage());
            }
            throw t;
        } finally {
            if (CollectionUtils.isNotEmpty(logables)) {
                for (Logable logable : logables) {
                    try {
                        logable.log(request, log.value(), param, result, success);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                StringBuilder consoleLog = new StringBuilder()
                        .append(StringUtils.LF)
                        .append("请求地址：")
                        .append(StringUtils.SPACE)
                        .append(request.getRequestURI())
                        .append(StringUtils.LF)
                        .append("请求结果：")
                        .append(StringUtils.LF)
                        .append(JsonUtil.toJsonString(result));
                logger.debug(consoleLog.toString());
            }
        }
    }
}
