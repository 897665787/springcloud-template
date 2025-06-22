package com.company.framework.gracefulresponse;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import com.company.common.util.JsonUtil;
import com.company.framework.gracefulresponse.exception.BusinessGRException;
import com.fasterxml.jackson.databind.JsonNode;
import com.feiniaojin.gracefulresponse.defaults.DefaultConstants;

import feign.FeignException;
import feign.Response;
import feign.Util;

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
			String code = jsonNode.get("code").asText();
			if (!DefaultConstants.DEFAULT_SUCCESS_CODE.equals(code)) {
				String msg = jsonNode.get("msg").asText();
//				FeignException.errorStatus("", response);
//				throw new FeignException(response.status(), msg);
				throw new BusinessGRException(Integer.valueOf(code), msg);
			}

			JsonNode data = jsonNode.get("data");

			String jsonString = JsonUtil.toJsonString(data);
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
