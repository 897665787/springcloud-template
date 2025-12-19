package com.company.framework.gracefulresponse.converter;

import com.company.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * GracefulResponse HttpMessageConverter，用于处理GracefulResponse框架返回的数据格式
 */
//@Component
public class GracefulResponseHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {
    
    public GracefulResponseHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // 不需要实现写操作
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readJsonObject(clazz, inputMessage);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Class<?> clazz = (type instanceof Class) ? (Class<?>) type : contextClass;
        return readJsonObject(clazz, inputMessage);
    }
    
    private Object readJsonObject(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        // 读取输入流中的JSON数据
        InputStream inputStream = inputMessage.getBody();
        String jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        // 解析JSON
        JsonNode responseJson = JsonUtil.toJsonNode(jsonString);
        if (responseJson == null) {
            return null;
        }
        
        // 检查是否包含code和data字段（GracefulResponse格式）
        if (!(responseJson.has("code") && responseJson.has("data"))) {
            // 不是GracefulResponse格式，直接解析为clazz类型
            return JsonUtil.toEntity(jsonString, clazz);
        }
        
        // 获取data节点
        JsonNode dataNode = responseJson.get("data");
        if (dataNode == null) {
            return null;
        }
        
        // 将data节点转换为clazz类型的对象
        String dataJsonStr = JsonUtil.toJsonString(dataNode);
        return JsonUtil.toEntity(dataJsonStr, clazz);
    }
}