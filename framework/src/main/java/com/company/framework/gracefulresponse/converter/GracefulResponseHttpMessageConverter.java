package com.company.framework.gracefulresponse.converter;

import cn.hutool.core.util.ObjectUtil;
import com.company.framework.gracefulresponse.context.GracefulResponseExceptionContext;
import com.company.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.data.Response;
import com.feiniaojin.gracefulresponse.data.ResponseStatus;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle0;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle1;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * GracefulResponse HttpMessageConverter，用于处理GracefulResponse框架返回的数据格式
 */
public class GracefulResponseHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {
    private final GracefulResponseProperties gracefulResponseProperties;

    public GracefulResponseHttpMessageConverter(GracefulResponseProperties gracefulResponseProperties) {
        super(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
        this.gracefulResponseProperties = gracefulResponseProperties;
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
        String typeName = type.getTypeName();
        if (type instanceof ParameterizedType) {
            // 处理List<T>泛型，获取T真正的class
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                typeName = actualTypeArguments[0].getTypeName();
            }
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        Class<?> clazz = (type instanceof Class) ? (Class<?>) type : contextClass;
        return readJsonObject(clazz, inputMessage);
    }

//    private Object readJsonObject(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
//        InputStream inputStream = inputMessage.getBody();
//        String responseBodyStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//
//        Response response = null;
//        String responseClassFullName = gracefulResponseProperties.getResponseClassFullName();
//        if (StringUtils.isNotBlank(responseClassFullName)) {
//            Class<?> cresponseClass = null;
//            try {
//                cresponseClass = Class.forName(responseClassFullName);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//            response = (Response) JsonUtil.toEntity(responseBodyStr, cresponseClass);
//        } else {
//            Integer responseStyle = gracefulResponseProperties.getResponseStyle();
//            if (responseStyle == null || responseStyle == 0) {
//                response = JsonUtil.toEntity(responseBodyStr, DefaultResponseImplStyle0.class);
//            } else {
//                response = JsonUtil.toEntity(responseBodyStr, DefaultResponseImplStyle1.class);
//            }
//        }
//        if (response == null) {
//            return null;
//        }
//        ResponseStatus status = response.getStatus();
//        if (status == null) {
//            return null;
//        }
//        String code = status.getCode();
//        String msg = status.getMsg();
//        Object data = response.getPayload();
//
//        // 是否包含code和msg字段或者是否包含code和data字段
//        if (!(StringUtils.isNotBlank(code) && (StringUtils.isNotBlank(msg) || data != null))) {
//            return toClassObject(responseJson, clazz);
//        }
//        if (StringUtils.isBlank(code)) {
//            return toClassObject(responseJson, clazz);
//        }
//
//        // 是GracefulResponse包装
//        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
//        if (!defaultSuccessCode.equals(code)) {
//            // 这里如果抛异常，就会被feign拦截掉。所以将异常设置到上下文，后续使用GracefulResponseFeignExceptionAspect切面处理异常抛出
//            GracefulResponseExceptionContext.setException(new GracefulResponseException(code, responseJson.get("msg").asText()));
//            return toClassObject(responseJson, clazz);
//        }
//        if (data == null) {
//            return toClassObject(responseJson, clazz);
//        }
//        return toClassObject(dataNode, clazz);
//    }
//
//    private Object toClassObject2(String bodyStr, Class<?> clazz) {
//        if (dataNode.isObject()) {
//            return JsonUtil.toEntity(bodyStr, clazz);
//        }
//        if (dataNode.isArray()) {
//            return JsonUtil.toList(bodyStr, clazz);
//        }
//        return null;
//    }

    private Object readJsonObject(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        InputStream inputStream = inputMessage.getBody();
        String responseBodyStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JsonNode responseJson = JsonUtil.toJsonNode(responseBodyStr);
        if (responseJson == null) {
            return null;
        }
        // 是否包含code和msg字段或者是否包含code和data字段
        if (!(responseJson.has("code") && (responseJson.has("msg") || responseJson.has("data")))) {
            return toClassObject(responseJson, clazz);
        }

        String code = responseJson.get("code").asText();
        if (StringUtils.isBlank(code)) {
            return toClassObject(responseJson, clazz);
        }

        // 是GracefulResponse包装
        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
        if (!defaultSuccessCode.equals(code)) {
            // 这里如果抛异常，就会被feign拦截掉。所以将异常设置到上下文，后续使用GracefulResponseFeignExceptionAspect切面处理异常抛出
            GracefulResponseExceptionContext.setException(new GracefulResponseException(code, responseJson.get("msg").asText()));
            return toClassObject(responseJson, clazz);
        }
        JsonNode dataNode = responseJson.get("data");
        if (dataNode == null) {
            return toClassObject(responseJson, clazz);
        }
        return toClassObject(dataNode, clazz);
    }

    private Object toClassObject(JsonNode dataNode, Class<?> clazz) {
        String dataJsonStr = JsonUtil.toJsonString(dataNode);
        if (dataNode.isObject()) {
            return JsonUtil.toEntity(dataJsonStr, clazz);
        }
        if (dataNode.isArray()) {
            return JsonUtil.toList(dataJsonStr, clazz);
        }
        return null;
    }

}
