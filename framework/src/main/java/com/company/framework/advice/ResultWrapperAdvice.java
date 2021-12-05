package com.company.framework.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.company.common.annotation.NoResultWrapper;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;

/**
 * 增强controller返回值，使用Result进行包装
 */
@Order(90)
@RestControllerAdvice(basePackages = { "com.company" }) // 注意哦，这里要加上需要扫描的包
public class ResultWrapperAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		// 如果使用了NoResultWrapper，说明响应值不需要使用Result包装
		NoResultWrapper noResultWrapper = returnType.getMethodAnnotation(NoResultWrapper.class);
		if (noResultWrapper != null) {
			return false;
		}
		// 如果接口返回的类型本身就是Result那就没有必要进行额外的操作，返回false
		boolean equals = returnType.getGenericParameterType().equals(Result.class);
		return !equals;
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		Result result = Result.success(data);
		if (returnType.getGenericParameterType().equals(String.class)) {
			// String类型不能直接包装，所以要进行些特别的处理
			return JsonUtil.toJsonString(result);
		}
		// 将原本的数据包装在Result里
		return result;
	}
}