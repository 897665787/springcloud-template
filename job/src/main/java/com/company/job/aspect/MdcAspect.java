package com.company.job.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.company.framework.filter.MdcUtil;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * MDC日志ID切面
 * 
 * @author JQ棣
 */
@Aspect
@Component
public class MdcAspect {

	@Around("@annotation(xxlJob)")
	public Object server(ProceedingJoinPoint joinPoint, XxlJob xxlJob) throws Throwable {
		MdcUtil.put();// 在jobHander执行之前塞入日志ID，用于追踪整个执行链路
		Object result = joinPoint.proceed();
		return result;
	}

}
