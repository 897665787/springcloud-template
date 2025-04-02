package com.company.framework.feign;

import com.company.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 记录请求Feign接口耗时，打印在调用方日志
 * 
 * @author JQ棣
 */
@Slf4j
@Aspect
//@Component
@Deprecated // FeignLoggerClient已经可以打印所需的所有日志信息
public class FeignCostTimeAspect {

	@Value("${template.requestFilter.arrMaxLength:1000}")
	private int arrMaxLength;
	
	@Around("@within(org.springframework.cloud.openfeign.FeignClient)")
	public Object feignCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		try {
			long end = System.currentTimeMillis();
			Signature signature = joinPoint.getSignature();
			log.info("request feign:{},mills:{},args:{},result:{}", signature.toShortString().replace("(..)", ""), end - start,
					JsonUtil.toJsonStringReplaceProperties(joinPoint.getArgs(), arrMaxLength), JsonUtil.toJsonStringReplaceProperties(result, arrMaxLength));
		} catch (Exception e) {
			log.error("request feign error", e);
		}
		return result;
	}
}
