package com.company.framework.gracefulresponse.converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feiniaojin.gracefulresponse.GracefulResponse;
import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.data.Response;
import com.feiniaojin.gracefulresponse.data.ResponseStatus;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle0;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle1;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseStatus;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

/**
 * GracefulResponse HttpMessageConverter，用于处理GracefulResponse框架返回的数据格式
 */
@Slf4j
public class GracefulResponseHttpMessageConverter2 extends MappingJackson2HttpMessageConverter {

    private static final Integer RESPONSE_STYLE_0 = 0;

    private static final Integer RESPONSE_STYLE_1 = 1;

    private final GracefulResponseProperties gracefulResponseProperties;

    public GracefulResponseHttpMessageConverter2(GracefulResponseProperties gracefulResponseProperties) {
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    public GracefulResponseHttpMessageConverter2(ObjectMapper objectMapper,
        GracefulResponseProperties gracefulResponseProperties) {
        super(objectMapper);
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(type, contextClass);
        // return readJavaType(javaType, inputMessage);
        Pair<Boolean, Object> pair = gracefulResponseValue(inputMessage, javaType);
        if (pair.getKey()) {
            return pair.getValue();
        }
        return super.read(type, contextClass, inputMessage);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
        throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(clazz, null);
        // return readJavaType(javaType, inputMessage);
        Pair<Boolean, Object> pair = gracefulResponseValue(inputMessage, javaType);
        if (pair.getKey()) {
            return pair.getValue();
        }
        return super.readInternal(clazz, inputMessage);
    }

    private Pair<Boolean, Object> gracefulResponseValue(HttpInputMessage inputMessage, JavaType javaType) throws IOException {
        ObjectMapper objectMapper = getObjectMapper();
        InputStream inputStream = inputMessage.getBody();
        JsonNode responseJson = objectMapper.readTree(inputStream);

        if (responseJson == null) {
            return new Pair<>(false, null);
        }
        Response response = newEmptyInstance(responseJson, objectMapper, inputStream);
        if (response == null) {
            return new Pair<>(false, null);
        }
        ResponseStatus status = response.getStatus();
        if (status == null) {
            return new Pair<>(false, null);
        }

        // 是否包含code字段
        String code = status.getCode();
        if (!StringUtils.hasText(code)) {
            return new Pair<>(false, null);
        }

        // 是否包含msg字段或者data字段
        String msg = status.getMsg();
        Object data = response.getPayload();
        if (!StringUtils.hasText(msg) && data == null) {
            return new Pair<>(false, null);
        }

        // 是GracefulResponse包装
        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
        if (!defaultSuccessCode.equals(code)) {
            GracefulResponse.raiseException(code, msg);
            return new Pair<>(false, null);
        }
        if (data == null) {
            return new Pair<>(true, null);
        }
        return new Pair<>(true, objectMapper.readValue(objectMapper.writeValueAsString(data), javaType));
    }

    public Response newEmptyInstance(JsonNode responseJson, ObjectMapper objectMapper, InputStream inputStream) {
        try {
            String responseClassFullName = gracefulResponseProperties.getResponseClassFullName();
            // 配置了Response的全限定名，即自定义了Response，用配置的进行返回
            if (StringUtils.hasLength(responseClassFullName)) {
                Class<?> responseClass = null;
                try {
                    responseClass = Class.forName(responseClassFullName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return (Response)objectMapper.readValue(inputStream, responseClass);
            } else {
                // 没有配Response的全限定名，则创建DefaultResponse
                return generateDefaultResponse(responseJson);
            }
        } catch (Exception e) {
            throw new GracefulResponseException("创建空的Response失败", e);
        }
    }

    private Response generateDefaultResponse(JsonNode responseJson) {
        Integer responseStyle = gracefulResponseProperties.getResponseStyle();
        // 默认的Response style，5.0以上是Style0，5.0（包括5.0）之后是Style1
        if (Objects.isNull(responseStyle)) {
            return newDefaultResponseImplStyle1(responseJson);
        }
        if (RESPONSE_STYLE_0.equals(responseStyle)) {
            return newDefaultResponseImplStyle0(responseJson);
        } else if (RESPONSE_STYLE_1.equals(responseStyle)) {
            return newDefaultResponseImplStyle1(responseJson);
        } else {
            log.error("不支持的Response style类型,responseStyle={}", responseStyle);
            throw new IllegalArgumentException("不支持的Response style类型");
        }
    }

    private Response newDefaultResponseImplStyle1(JsonNode responseJson) {
        String code = Optional.ofNullable(responseJson.get("code")).map(JsonNode::asText).orElse(null);
        String msg = Optional.ofNullable(responseJson.get("msg")).map(JsonNode::asText).orElse(null);
        DefaultResponseImplStyle1 defaultResponseImplStyle1 = new DefaultResponseImplStyle1();
        defaultResponseImplStyle1.setStatus(new DefaultResponseStatus());
        defaultResponseImplStyle1.setCode(code);
        defaultResponseImplStyle1.setMsg(msg);
        defaultResponseImplStyle1.setData(responseJson.get("data"));
        return defaultResponseImplStyle1;
    }

    private Response newDefaultResponseImplStyle0(JsonNode responseJson) {
        JsonNode statusNode = responseJson.get("status");
        String code = Optional.ofNullable(statusNode).map(v -> v.get("code")).map(JsonNode::asText).orElse(null);
        String msg = Optional.ofNullable(statusNode).map(v -> v.get("msg")).map(JsonNode::asText).orElse(null);
        DefaultResponseImplStyle0 defaultResponseImplStyle0 = new DefaultResponseImplStyle0();
        ResponseStatus status = new DefaultResponseStatus(code, msg);
        defaultResponseImplStyle0.setStatus(status);
        defaultResponseImplStyle0.setPayload(responseJson.get("payload"));
        return defaultResponseImplStyle0;
    }
}
