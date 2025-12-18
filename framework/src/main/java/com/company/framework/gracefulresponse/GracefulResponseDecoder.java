package com.company.framework.gracefulresponse;

import com.company.common.util.JsonUtil;
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
import java.lang.reflect.Type;

public class GracefulResponseDecoder extends SpringDecoder {

    private GracefulResponseProperties gracefulResponseProperties;

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
        String responseBodyStr = Util.toString(response.body().asReader());
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
            Class<?> clazz = Class.forName(typeName);
            return JsonUtil.toEntity(dataJsonStr, clazz);
        } catch (ClassNotFoundException e) {
            return super.decode(response, type);
        }
    }
}
