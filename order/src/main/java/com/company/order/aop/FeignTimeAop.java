package com.company.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeignTimeAop {
	@Around("execution(public * com.company.*.api.feign.*.*(..))")
    public Object logServiceMethodAccess(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();
		Object object = joinPoint.proceed();
		long end = System.currentTimeMillis();
		long takeTime = end - start;
//		String className = joinPoint.getSignature().toString();
		System.out.println("FeignTimeAop.logServiceMethodAccess takeTime:" + takeTime);

        return object;
    }
}
