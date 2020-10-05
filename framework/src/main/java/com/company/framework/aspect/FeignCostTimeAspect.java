package com.company.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 记录请求Feign接口耗时，打印在调用方日志
 * 
 * @author jqd
 */
@Slf4j
@Aspect
@Component
public class FeignCostTimeAspect {
	
	@Around("@within(org.springframework.cloud.openfeign.FeignClient)")
	public Object feignCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		try {
			long end = System.currentTimeMillis();
			Signature signature = joinPoint.getSignature();
			log.info("request feign:{},mills:{},args:{},result:{}", signature.toShortString(), end - start,
					JSON.toJSONString(joinPoint.getArgs()), JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("request feign error", e);
		}
		return result;
	}
}
