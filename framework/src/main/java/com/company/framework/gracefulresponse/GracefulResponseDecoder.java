package com.company.framework.gracefulresponse;

import com.company.common.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import feign.Response;
import feign.Util;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

public class GracefulResponseDecoder extends SpringDecoder {
	public GracefulResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}

	public GracefulResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                                   ObjectProvider<HttpMessageConverterCustomizer> customizers) {
		super(messageConverters, customizers);
	}

	@Override
	public Object decode(final Response response, Type type) throws IOException, FeignException {
		String typeName = type.getTypeName();
		if (true) {
			// 解析响应体并转换为 BaseResponse<T>
			String json = Util.toString(response.body().asReader());

			JsonNode jsonNode = JsonUtil.toJsonNode(json);// 确保json格式正确
			JsonNode entity2 = jsonNode.get("data");

			String jsonString = JsonUtil.toJsonString(entity2);
			try {
				Class<?> aClass1 = Class.forName(typeName);
				System.out.println("Class: " + aClass1.getName());
				Object entity = JsonUtil.toEntity(jsonString, aClass1);
				return entity;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return super.decode(response, type);
	}
}