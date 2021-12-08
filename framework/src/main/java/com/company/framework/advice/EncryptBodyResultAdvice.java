package com.company.framework.advice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.company.common.annotation.EncryptResultData;
import com.company.common.api.Result;

import cn.hutool.crypto.SecureUtil;

/**
 * 对响应的Result中的data进行加密（该类一般用于最外层，最可能用于网关）
 */
@Order(1)
@RestControllerAdvice(basePackages = { "com.company" }) // 注意哦，这里要加上需要扫描的包
public class EncryptBodyResultAdvice implements ResponseBodyAdvice<Object> {
	private static final String ENCRYPT_KEY = "11111111";
	
	@Value("${template.enable.data-encypt:false}")
	private Boolean enable;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		if (!enable) {
			// 加密开关
			return false;
		}
		// 如果使用了EncryptResultData，说明响应值需要加密
		EncryptResultData encryptResultData = returnType.getMethodAnnotation(EncryptResultData.class);
		return encryptResultData != null;
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		if (data == null) {
			return data;
		}
		if (data instanceof Result) {
			Result result = (Result) data;
			Object data2 = result.getData();
			if (data2 != null) {
				String encryptData = SecureUtil.des(ENCRYPT_KEY.getBytes()).encryptBase64(String.valueOf(data2));
				result.setData(encryptData);
			}
			return result;
		}
		String encrypt = SecureUtil.des(ENCRYPT_KEY.getBytes()).encryptBase64(String.valueOf(data));
		return encrypt;
	}
}