package com.company.framework.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;

/**
 * 增强controller返回值进行包装
 */
@RestControllerAdvice(basePackages = { "com.company.web.controller" }) // 注意哦，这里要加上需要扫描的包
public class ResponseBodyResultAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		// 如果接口返回的类型本身就是Result那就没有必要进行额外的操作，返回false
		System.out.println("ResponseBodyResultAdvice.supports():" + returnType);
		boolean equals = returnType.getGenericParameterType().equals(Result.class);
		System.out.println("ResponseBodyResultAdvice.supports() equals:" + equals);
		return !equals;
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		System.out.println("ResponseBodyResultAdvice.beforeBodyWrite():" + data);
		if (returnType.getGenericParameterType().equals(String.class)) {
			// String类型不能直接包装，所以要进行些特别的处理
			return JsonUtil.toJsonString(Result.success(data));
		}
		// 将原本的数据包装在Result里
		return Result.success(data);
	}
}