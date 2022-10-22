package com.company.admin.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.company.common.api.ResultCode;
import com.company.common.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 根据code为响应值添加status字段,code=0:true,code=其他:false
 */
@Order(100)
@RestControllerAdvice(basePackages = { "com.company" }) // 注意哦，这里要加上需要扫描的包
public class StatusResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		String dataStr = null;
		if (data instanceof String) {
			dataStr = (String) data;
		} else {
			dataStr = JsonUtil.toJsonString(data);
		}
		JsonNode resultNode = JsonUtil.readTree(dataStr);
		int code = resultNode.get("code").asInt();
		ObjectNode putResultNode = (ObjectNode) resultNode;
		putResultNode.put("status", code == ResultCode.SUCCESS.getCode());
		return putResultNode;
	}
}