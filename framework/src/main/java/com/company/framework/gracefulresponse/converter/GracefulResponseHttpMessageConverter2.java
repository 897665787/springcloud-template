package com.company.framework.gracefulresponse.converter;

import com.company.framework.gracefulresponse.context.GracefulResponseExceptionContext;
import com.company.framework.util.JsonUtil;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.feiniaojin.gracefulresponse.GracefulResponseException;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.data.Response;
import com.feiniaojin.gracefulresponse.data.ResponseStatus;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle0;
import com.feiniaojin.gracefulresponse.defaults.DefaultResponseImplStyle1;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * GracefulResponse HttpMessageConverter，用于处理GracefulResponse框架返回的数据格式
 */
public class GracefulResponseHttpMessageConverter2 extends MappingJackson2HttpMessageConverter {
    private static final Map<String, JsonEncoding> ENCODINGS;

    static {
        ENCODINGS = CollectionUtils.newHashMap(JsonEncoding.values().length);
        for (JsonEncoding encoding : JsonEncoding.values()) {
            ENCODINGS.put(encoding.getJavaName(), encoding);
        }
        ENCODINGS.put("US-ASCII", JsonEncoding.UTF8);
    }

    private final GracefulResponseProperties gracefulResponseProperties;

    public GracefulResponseHttpMessageConverter2(GracefulResponseProperties gracefulResponseProperties) {
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    public GracefulResponseHttpMessageConverter2(ObjectMapper objectMapper, GracefulResponseProperties gracefulResponseProperties) {
        super(objectMapper);
        this.gracefulResponseProperties = gracefulResponseProperties;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(clazz, null);
        readJavaType(javaType, inputMessage);
        return super.readInternal(clazz, inputMessage);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(type, contextClass);
        readJavaType(javaType, inputMessage);
        return super.read(type, contextClass, inputMessage);
    }

    private Object readJsonObject(Type type, Class<?> contextClass, HttpInputMessage inputMessage, Class<?> clazz) throws IOException {
        InputStream inputStream = inputMessage.getBody();
        String responseBodyStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        Response response = null;
        String responseClassFullName = gracefulResponseProperties.getResponseClassFullName();
        if (StringUtils.isNotBlank(responseClassFullName)) {
            Class<?> cresponseClass = null;
            try {
                cresponseClass = Class.forName(responseClassFullName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            response = (Response) JsonUtil.toEntity(responseBodyStr, cresponseClass);
        } else {
            Integer responseStyle = gracefulResponseProperties.getResponseStyle();
            if (responseStyle == null || responseStyle == 0) {
                response = JsonUtil.toEntity(responseBodyStr, DefaultResponseImplStyle0.class);
            } else {
                response = JsonUtil.toEntity(responseBodyStr, DefaultResponseImplStyle1.class);
            }
        }
        if (response == null) {
            return super.read(type, contextClass, inputMessage);
        }
        ResponseStatus status = response.getStatus();
        if (status == null) {
            return super.read(type, contextClass, inputMessage);
        }
        String code = status.getCode();
        String msg = status.getMsg();
        Object data = response.getPayload();

        // 是否包含code和msg字段或者是否包含code和data字段
        if (!(StringUtils.isNotBlank(code) && (StringUtils.isNotBlank(msg) || data != null))) {
            return super.read(type, contextClass, inputMessage);
        }
        if (StringUtils.isBlank(code)) {
            return super.read(type, contextClass, inputMessage);
        }

        // 是GracefulResponse包装
        String defaultSuccessCode = gracefulResponseProperties.getDefaultSuccessCode();
        if (!defaultSuccessCode.equals(code)) {
            // 这里如果抛异常，就会被feign拦截掉。所以将异常设置到上下文，后续使用GracefulResponseFeignExceptionAspect切面处理异常抛出
            GracefulResponseExceptionContext.setException(new GracefulResponseException(code, responseJson.get("msg").asText()));
            return toClassObject(responseJson, clazz);
        }
        if (data == null) {
            return super.read(type, contextClass, inputMessage);
        }
        return super.read(type, contextClass, inputMessage);
    }

    private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) throws IOException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = getCharset(contentType);

//        ObjectMapper objectMapper = selectObjectMapper(javaType.getRawClass(), contentType);
        ObjectMapper objectMapper = getObjectMapper();
        Assert.state(objectMapper != null, () -> "No ObjectMapper for " + javaType);

        boolean isUnicode = ENCODINGS.containsKey(charset.name()) ||
                "UTF-16".equals(charset.name()) ||
                "UTF-32".equals(charset.name());
        try {
            InputStream inputStream = StreamUtils.nonClosing(inputMessage.getBody());
            if (inputMessage instanceof MappingJacksonInputMessage) {
                Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    ObjectReader objectReader = objectMapper.readerWithView(deserializationView).forType(javaType);
                    if (isUnicode) {
                        return objectReader.readValue(inputStream);
                    }
                    else {
                        Reader reader = new InputStreamReader(inputStream, charset);
                        return objectReader.readValue(reader);
                    }
                }
            }
            if (isUnicode) {
                return objectMapper.readValue(inputStream, javaType);
            }
            else {
                Reader reader = new InputStreamReader(inputStream, charset);
                return objectMapper.readValue(reader, javaType);
            }
        }
        catch (InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        }
        catch (JsonProcessingException ex) {
            throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex, inputMessage);
        }
    }

}
