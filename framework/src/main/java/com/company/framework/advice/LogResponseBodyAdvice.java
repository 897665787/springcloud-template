package com.company.framework.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.company.common.constant.CommonConstants;
import com.company.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应值日志打印
 */
@Slf4j
@Order(99)
@RestControllerAdvice(basePackages = { CommonConstants.BASE_PACKAGE }) // 注意哦，这里要加上需要扫描的包
public class LogResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		log.info("{}", data instanceof String ? data : JsonUtil.toJsonString(data));
		return data;
	}
}