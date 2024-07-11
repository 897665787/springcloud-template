package com.company.order.retry.method;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.company.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallableRetryMethod implements RetryMethod<Callable<String>> {

	@Override
	public RetryResult invoke(Callable<String> method, Object params, int failure, int maxFailure) {
		RetryResult retryResult = new RetryResult();

		String message = null;
		boolean abandonCallback = false;

		log.info(failure + "回调,请求地址:{},参数:{}", failure, method, JsonUtil.toJsonString(params));
		long start = System.currentTimeMillis();

		try {
			String result = method.call();
			System.out.println(result);
			retryResult.setSuccess(true);
			retryResult.setMessage(message);
			return retryResult;
		} catch (Exception e) {
			log.error("{}ms,回调异常", System.currentTimeMillis() - start, e);
			message = ExceptionUtils.getMessage(e);
		}
		retryResult.setSuccess(false);
		retryResult.setMessage(message);
		retryResult.setAbandonCallback(abandonCallback);
		return retryResult;
	}
}
