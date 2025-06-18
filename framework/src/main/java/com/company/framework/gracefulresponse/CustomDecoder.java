package com.company.framework.gracefulresponse;

import com.company.common.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * feign调用过程中传递header值
 *
 * 依赖Hystrix自定义并发策略:TransferHystrixConcurrencyStrategy
 */
public class CustomDecoder implements Decoder {

//	private final Decoder decoder;

	public CustomDecoder() {
//		this.decoder = new JacksonDecoder();
	}

	@Override
	public Object decode(Response response, Type type) throws IOException, DecodeException {
		// 解析响应体并转换为 BaseResponse<T>
		String json = Util.toString(response.body().asReader());

		JsonNode jsonNode = JsonUtil.toJsonNode(json);// 确保json格式正确
		JsonNode entity2 = jsonNode.get("data");

		String jsonString = JsonUtil.toJsonString(entity2);

		String typeName = type.getTypeName();
        try {
            Class<?> aClass1 = Class.forName(typeName);
			System.out.println("Class: " + aClass1.getName());
			Object entity = JsonUtil.toEntity(jsonString, aClass1);
			return entity;
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//        ResponseStatus entity = JsonUtil.toEntity(jsonString, DefaultResponseStatus.class);
	}
}