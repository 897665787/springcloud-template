package com.company.framework.feign;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * copy from RestTemplate
 * 增加了日志打印和公共请求头传递
 */
@Slf4j
public class TraceRestTemplate extends RestTemplate {
    private final TraceManager traceManager;

    public TraceRestTemplate(ClientHttpRequestFactory requestFactory, TraceManager traceManager) {
        super(requestFactory);
        this.traceManager = traceManager;
    }

    /**
     * Return a {@code RequestCallback} implementation that writes the given
     * object to the request stream.
     */
    public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody) {
        return new HttpEntityRequestCallback(requestBody);
    }

    /**
     * Return a {@code RequestCallback} implementation that:
     * <ol>
     * <li>Sets the request {@code Accept} header based on the given response
     * type, cross-checked against the configured message converters.
     * <li>Writes the given object to the request stream.
     * </ol>
     */
    public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

    /**
     * Execute the given method on the provided URI.
     * <p>The {@link ClientHttpRequest} is processed using the {@link RequestCallback};
     * the response with the {@link ResponseExtractor}.
     *
     * @param url               the fully-expanded URL to connect to
     * @param method            the HTTP method to execute (GET, POST, etc.)
     * @param requestCallback   object that prepares the request (can be {@code null})
     * @param responseExtractor object that extracts the return value from the response (can be {@code null})
     * @return an arbitrary object, as returned by the {@link ResponseExtractor}
     */
    @Nullable
    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);

            Map<String, List<String>> headers = Maps.newHashMap();
            // 请求上下文中传递到下游的相关headers
            headers.putAll(HttpContextUtil.httpContextHeaders());
            // 日志追踪ID
            headers.putAll(traceManager.headers());
            HttpHeaders httpHeaders = request.getHeaders();
            if (!headers.isEmpty()) {
                httpHeaders.putAll(headers);
            }

            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            long start = System.currentTimeMillis();
            response = request.execute();
            long end = System.currentTimeMillis();
            handleResponse(url, method, response);

            int arrMaxLength = SpringContextUtil.getIntegerProperty("template.requestFilter.arrMaxLength", 1000);
            String headersStr = JsonUtil.toJsonStringReplaceProperties(httpHeaders.toSingleValueMap(), arrMaxLength);

            String body = "{}";
            if (requestCallback instanceof HttpEntityRequestCallback) {
                HttpEntityRequestCallback httpEntityRequestCallback = (HttpEntityRequestCallback) requestCallback;
                HttpEntity<?> requestEntity = httpEntityRequestCallback.requestEntity;
                body = JsonUtil.toJsonStringReplaceProperties(requestEntity.getBody(), arrMaxLength);
            }

            T responseObj = responseExtractor != null ? responseExtractor.extractData(response) : null;
            String result = JsonUtil.toJsonStringReplaceProperties(responseObj, arrMaxLength);
            log.info("{}ms {} {} headers:{},body:{},result:{}", end - start, method.name(), url, headersStr, body, result);
            return responseObj;
        } catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Request callback implementation that writes the given object to the request stream.
     */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        public HttpEntityRequestCallback(@Nullable Object requestBody) {
            this(requestBody, null);
        }

        public HttpEntityRequestCallback(@Nullable Object requestBody, @Nullable Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            }
            else if (requestBody != null) {
                this.requestEntity = new HttpEntity<>(requestBody);
            }
            else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            Object requestBody = this.requestEntity.getBody();
            if (requestBody == null) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                }
                if (httpHeaders.getContentLength() < 0) {
                    httpHeaders.setContentLength(0L);
                }
            }
            else {
                Class<?> requestBodyClass = requestBody.getClass();
                Type requestBodyType = (this.requestEntity instanceof RequestEntity ?
                        ((RequestEntity<?>)this.requestEntity).getType() : requestBodyClass);
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : getMessageConverters()) {
                    if (messageConverter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<Object> genericConverter =
                                (GenericHttpMessageConverter<Object>) messageConverter;
                        if (genericConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                            if (!requestHeaders.isEmpty()) {
                                requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                            }
                            logBody(requestBody, requestContentType, genericConverter);
                            genericConverter.write(requestBody, requestBodyType, requestContentType, httpRequest);
                            return;
                        }
                    }
                    else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                        }
                        logBody(requestBody, requestContentType, messageConverter);
                        ((HttpMessageConverter<Object>) messageConverter).write(
                                requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "No HttpMessageConverter for " + requestBodyClass.getName();
                if (requestContentType != null) {
                    message += " and content type \"" + requestContentType + "\"";
                }
                throw new RestClientException(message);
            }
        }

        private void logBody(Object body, @Nullable MediaType mediaType, HttpMessageConverter<?> converter) {
            if (logger.isDebugEnabled()) {
                if (mediaType != null) {
                    logger.debug("Writing [" + body + "] as \"" + mediaType + "\"");
                }
                else {
                    logger.debug("Writing [" + body + "] with " + converter.getClass().getName());
                }
            }
        }
    }

    /**
     * Request callback implementation that prepares the request's accept headers.
     */
    private class AcceptHeaderRequestCallback implements RequestCallback {

        @Nullable
        private final Type responseType;

        public AcceptHeaderRequestCallback(@Nullable Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                List<MediaType> allSupportedMediaTypes = getMessageConverters().stream()
                        .filter(converter -> canReadResponse(this.responseType, converter))
                        .flatMap((HttpMessageConverter<?> converter) -> getSupportedMediaTypes(this.responseType, converter))
                        .distinct()
                        .sorted(MediaType.SPECIFICITY_COMPARATOR)
                        .collect(Collectors.toList());
                if (logger.isDebugEnabled()) {
                    logger.debug("Accept=" + allSupportedMediaTypes);
                }
                request.getHeaders().setAccept(allSupportedMediaTypes);
            }
        }

        private boolean canReadResponse(Type responseType, HttpMessageConverter<?> converter) {
            Class<?> responseClass = (responseType instanceof Class ? (Class<?>) responseType : null);
            if (responseClass != null) {
                return converter.canRead(responseClass, null);
            }
            else if (converter instanceof GenericHttpMessageConverter) {
                GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                return genericConverter.canRead(responseType, null, null);
            }
            return false;
        }

        private Stream<MediaType> getSupportedMediaTypes(Type type, HttpMessageConverter<?> converter) {
            Type rawType = (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
            Class<?> clazz = (rawType instanceof Class ? (Class<?>) rawType : null);
            return (clazz != null ? converter.getSupportedMediaTypes(clazz) : converter.getSupportedMediaTypes())
                    .stream()
                    .map(mediaType -> {
                        if (mediaType.getCharset() != null) {
                            return new MediaType(mediaType.getType(), mediaType.getSubtype());
                        }
                        return mediaType;
                    });
        }
    }

}
