package com.company.framework.gracefulresponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

//@Component
public class NestedResponseMessageConverter extends AbstractHttpMessageConverter<Object> {

	public NestedResponseMessageConverter() {
		super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		// 获取实际的泛型类型
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];

		// 读取原始JSON
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> rawResponse = objectMapper.readValue(inputMessage.getBody(), new TypeReference<Map<String, Object>>() {});

		// 提取嵌套的data字段
		Map<String, Object> outerData = (Map<String, Object>) rawResponse.get("data");

		// 如果Result的泛型是另一个Result（即Result<Result<T>>的情况）
		if (clazz.getTypeName().contains("Result") && outerData.containsKey("data")) {
			return objectMapper.convertValue(outerData, clazz);
		}

		// 普通情况：将data字段转换为目标类型
		return objectMapper.convertValue(outerData, objectMapper.constructType(type));
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// 不需要实现，因为这是Feign客户端
		System.out.println("writeInternal");
    }
}