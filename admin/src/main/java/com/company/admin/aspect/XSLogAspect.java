package com.company.admin.aspect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.company.admin.entity.base.XSGenericModel;
import com.company.admin.exception.ExceptionConsts;
import com.company.common.api.Result;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.util.IpUtil;

/**
 * 日志切面
 * Created by xuxiaowei on 2017/10/20.
 */
@Service
@Aspect
public class XSLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(XSLogAspect.class);

    @Autowired(required = false)
    private XSLogHandler xsLogHandler;

    @Around("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public Object doAroud(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = HttpContextUtil.request();

        XSLogger xsLogger = new XSLogger();
        xsLogger.setRequestTime(new Date());
        xsLogger.setRequestIp(IpUtil.getRequestIp(request));
        xsLogger.setHost(IpUtil.getHostIp());
        xsLogger.setRequestMethod(request.getMethod());
        xsLogger.setRequestUrl(request.getRequestURI());
        xsLogger.setStatus(true);

        List<Object> parameters = new ArrayList<>();
        for (Object o : joinPoint.getArgs()) {
            if (o instanceof XSGenericModel) {
                parameters.add(o);
            }
        }
        xsLogger.setParameters(parameters);

        Object result = null;
        try {
            result = ((ProceedingJoinPoint) joinPoint).proceed();
        } catch (Throwable t) {
            xsLogger.setStatus(false);
            if (t instanceof BusinessException) {
            	BusinessException e = (BusinessException) t;
				result = Result.fail(e);
            }
            else if (t instanceof DuplicateKeyException) {
                result = Result.fail(ExceptionConsts.SQL_DUPLICATE_KEY);
            }
            else {
                result = Result.fail(ExceptionConsts.FAILURE);
            }
            throw t;
        } finally {
            String resultStr = null;
            if (result instanceof ModelAndView) {
                resultStr = ((ModelAndView) result).getViewName();
            }
            else {
                try {
                    resultStr = JsonUtil.toJsonString(result);
                } catch (Exception e) {
                    logger.error("error : ", e);
                }
            }
            if (resultStr != null) {
                if (resultStr.length() > 200) {
                    resultStr = resultStr.substring(0, 100) + "...log too large..." + resultStr.substring(resultStr.length() - 100);
                }
                xsLogger.setResult(resultStr);
            }

            Float executionTime = (System.currentTimeMillis() - xsLogger.getRequestTime().getTime()) / 1000F;
            xsLogger.setExecutionDuration(String.valueOf(executionTime) + "s");

            xsLogger.log();

            if (xsLogHandler != null) {
                try {
                    xsLogHandler.handle(xsLogger);
                } catch (Exception e) {
                    logger.error("error : ", e);
                }
            }
        }
        return result;
    }
}
