package com.company.framework.gracefulresponse.feign;

import com.company.framework.gracefulresponse.context.GracefulResponseExceptionContext;
import com.company.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import feign.FeignException;
import feign.Response;
import feign.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Feign的GracefulResponse解码器
 */
public class GracefulResponseDecoder extends SpringDecoder {

    private final GracefulResponseProperties gracefulResponseProperties;

    public GracefulResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters, GracefulResponseProperties gracefulResponseProperties) {
        super(messageConverters);
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    public GracefulResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectProvider<HttpMessageConverterCustomizer> customizers, GracefulResponseProperties gracefulResponseProperties) {
        super(messageConverters, customizers);
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        String responseBodyStr = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        JsonNode responseJson = JsonUtil.toJsonNode(responseBodyStr);
        if (responseJson == null) {
            return super.decode(response, type);
        }
        // 是否包含code和msg字段或者是否包含code和data字段
        if (!(responseJson.has("code") && (responseJson.has("msg") || responseJson.has("data")))) {
            return super.decode(response, type);
        }

        String code = responseJson.get("code").asText();
        if (StringUtils.isBlank(code)) {
            return super.decode(response, type);
        }

        // 是GracefulResponse包装
        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
        if (!defaultSuccessCode.equals(code)) {
            // 这里如果抛异常，就会被feign拦截掉。所以将异常设置到上下文，后续使用GracefulResponseFeignExceptionAspect切面处理异常抛出
            GracefulResponseExceptionContext.setException(new GracefulResponseException(code, responseJson.get("msg").asText()));
            return null;
        }
        JsonNode dataNode = responseJson.get("data");
        if (dataNode == null) {
            return null;
        }
        String dataJsonStr = JsonUtil.toJsonString(dataNode);
        try {
            String typeName = type.getTypeName();
            if (dataNode.isObject()) {
                Class<?> clazz = Class.forName(typeName);
                return JsonUtil.toEntity(dataJsonStr, clazz);
            }
            if (dataNode.isArray()) {
                if (type instanceof ParameterizedType) {
                    // 处理List<T>泛型，获取T真正的class
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        typeName = actualTypeArguments[0].getTypeName();
                    }
                }
                Class<?> clazz = Class.forName(typeName);
                return JsonUtil.toList(dataJsonStr, clazz);
            }
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        return super.decode(response, type);
    }
}
