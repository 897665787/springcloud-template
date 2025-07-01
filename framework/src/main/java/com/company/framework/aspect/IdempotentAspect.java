package com.company.framework.aspect;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.company.common.annotation.Idempotent;
import com.company.framework.util.JsonUtil;
import com.company.framework.cache.ICache;
import com.company.framework.lock.LockClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 幂等切面
 *
 * @author JQ棣
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "idempotent", havingValue = "true", matchIfMissing = false)
public class IdempotentAspect implements InitializingBean {

	private boolean needIdempotent = false;

	@Autowired
	private ICache cache;
	@Autowired
	private LockClient lockClient;

	/**
	 * 发起feign请求的服务
	 *
	 * @param joinPoint
	 * @param idempotent
	 * @return
	 * @throws Throwable
	 */
	@Around("@within(org.springframework.cloud.openfeign.FeignClient) && @annotation(idempotent)")
	public Object server(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
		if (!needIdempotent) {
			return joinPoint.proceed();
		}

		IdempotentUtil.create();
		Object result = joinPoint.proceed();
		IdempotentUtil.remove();
		return result;
	}

	/**
	 * 接收feign请求的服务
	 *
	 * @param joinPoint
	 * @param idempotent
	 * @return
	 * @throws Throwable
	 */
	@Around("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && @annotation(idempotent)")
	public Object client(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
		String idempotentId = IdempotentUtil.idempotentId();
		if (idempotentId == null) {
			return joinPoint.proceed();
		}
		return lockClient.doInLock(IdempotentUtil.lock(), () -> {
			Boolean success = cache.del(IdempotentUtil.head(idempotentId));// 删除成功代表首次执行
			if (success) {
				Object result = null;
				try {
					result = joinPoint.proceed();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
				if (result != null) {
					// 将结果保存在redis，供重试请求直接从redis获取
					cache.set(IdempotentUtil.data(), result, IdempotentUtil.idempotentExpireMillis(),
							TimeUnit.MILLISECONDS);
				}
				return result;
			} else {
				Signature signature = joinPoint.getSignature();
				MethodSignature methodSignature = (MethodSignature) signature;

				Class<?> returnType = methodSignature.getReturnType();
				if ("void".equals(returnType.getName())) {
					log.info("already execute:{}", idempotentId);
					return null;
				}
				Object result = cache.get(IdempotentUtil.data(), returnType);
				log.info("already execute:{},result:{}", idempotentId, JsonUtil.toJsonString(result));
				return result;
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		needIdempotent = IdempotentUtil.retries() > 1;
	}
}
