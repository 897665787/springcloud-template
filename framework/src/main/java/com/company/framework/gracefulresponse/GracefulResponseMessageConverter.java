package com.company.framework.gracefulresponse;

import cn.hutool.core.io.IoUtil;
import com.company.common.util.JsonUtil;
import com.company.framework.gracefulresponse.exception.BusinessFeignException;
import com.fasterxml.jackson.databind.JsonNode;
import com.feiniaojin.gracefulresponse.defaults.DefaultConstants;
import feign.Util;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class GracefulResponseMessageConverter implements GenericHttpMessageConverter<Object> {
    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.getName().startsWith("com.company")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        //
        String typeName = type.getTypeName();
        InputStream body = inputMessage.getBody();
        String json = IoUtil.readUtf8(body);
        if (true) {
            // 解析响应体并转换为 BaseResponse<T>
//            String json = Util.toString(response.body().asReader());

            JsonNode jsonNode = JsonUtil.toJsonNode(json);// 确保json格式正确
            String code = jsonNode.get("code").asText();
            if (!DefaultConstants.DEFAULT_SUCCESS_CODE.equals(code)) {
                String msg = jsonNode.get("msg").asText();
//				FeignException.errorStatus("", response);
//				throw new FeignException(response.status(), msg);
//				GracefulResponse.raiseException(code,msg);
//				return null;
                throw new BusinessFeignException(Integer.valueOf(code), msg);
//				throw new BusinessGRException(Integer.valueOf(code), msg);
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
        return null;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public void write(Object s, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return null;
    }

    @Override
    public String read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Object s, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
