package com.company.order.retry.method;

import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.order.innercallback.header.HeaderName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestTemplateRetryMethod implements RetryMethod<String> {

	private RestTemplate restTemplate;

	public RestTemplateRetryMethod(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public RetryResult invoke(String method, Object params, int failure, int maxFailure) {
		RetryResult retryResult = new RetryResult();

		String message = null;
		boolean abandonCallback = false;

		log.info("回调,请求地址:{},参数:{}", method, JsonUtil.toJsonString(params));
		long start = System.currentTimeMillis();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HeaderName.FAILURE, String.valueOf(failure));
			headers.add(HeaderName.MAX_FAILURE, String.valueOf(maxFailure));
			MdcUtil.headers2().forEach((k, v) -> headers.addAll(k, v));// 日志追踪ID
			HttpEntity<Object> httpEntity = new HttpEntity<>(params, headers);
			@SuppressWarnings("rawtypes")
			ResponseEntity<Result> responseEntity = restTemplate.postForEntity(method, httpEntity, Result.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				@SuppressWarnings("unchecked")
				Result<Boolean> result = responseEntity.getBody();
				log.info("{}ms,回调结果:{}", System.currentTimeMillis() - start, JsonUtil.toJsonString(result));
				message = result.getMessage();
				if (result.successCode()) {
					retryResult.setSuccess(true);
					retryResult.setMessage(message);
					return retryResult;
				} else {
					Boolean retry = result.getData();
					abandonCallback = Optional.ofNullable(retry).orElse(abandonCallback);
				}
			} else {
				message = "响应码:" + responseEntity.getStatusCodeValue();
			}
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
