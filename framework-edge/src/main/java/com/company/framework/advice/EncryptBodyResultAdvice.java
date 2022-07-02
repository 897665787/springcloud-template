package com.company.framework.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import com.company.common.util.JsonUtil;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 对响应的Result中的data进行加密（该类一般用于最外层，最可能用于网关）
 */
@Slf4j
@Order(1)
@RestControllerAdvice(basePackages = { "com.company" }) // 注意哦，这里要加上需要扫描的包
@ConditionalOnProperty(prefix = "template.enable", name = "data-encypt", havingValue = "true", matchIfMissing = false)
public class EncryptBodyResultAdvice implements ResponseBodyAdvice<Object> {
	private static final String ENCRYPT_KEY = "11111111";

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
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
			Result<?> result = (Result<?>) data;
			if (!result.successCode()) {
				return result;
			}
			Object data2 = result.getData();
			if (data2 == null) {
				return result;
			}
			String encryptData = SecureUtil.des(ENCRYPT_KEY.getBytes()).encryptBase64(String.valueOf(data2));
			Result<String> encryptResult = Result.success(encryptData).setMessage(result.getMessage());
			log.info("原数据:{},加密后数据:{}", JsonUtil.toJsonString(data), JsonUtil.toJsonString(encryptResult));
			return encryptResult;
		}
		
		String encryptData = SecureUtil.des(ENCRYPT_KEY.getBytes()).encryptBase64(String.valueOf(data));
		log.info("原数据:{},加密后数据:{}", data instanceof String ? data : JsonUtil.toJsonString(data), encryptData);
		return encryptData;
	}
}