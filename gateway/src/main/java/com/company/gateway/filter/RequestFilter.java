package com.company.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.company.gateway.constant.CommonConstants;
import com.company.gateway.constant.HeaderConstants;
import com.company.gateway.trace.TraceManager;
import com.company.gateway.util.IpUtil;
import com.company.gateway.util.JsonUtil;
import com.company.gateway.util.WebUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestFilter implements GlobalFilter, Ordered {
	private final TraceManager traceManager;

	@Value("${filter.request.enable:true}")
	private boolean enable;

    @Override
    public int getOrder() {
        return CommonConstants.FilterOrdered.REQUEST;
    }

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (!enable) {
			return chain.filter(exchange);
		}
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String requestIp = IpUtil.getRequestIp(request);
		String headerStr = JsonUtil.toJsonString(headers.toSingleValueMap());
		HttpMethod method = request.getMethod();
		String path = request.getURI().getPath();
		try {
			String paramsStr = JsonUtil.toJsonString(WebUtil.getReqParam(request));
			String uniqueKey = request.getHeaders().getFirst(HeaderConstants.TRACE_ID);

			String contentType = Optional.ofNullable(headers).map(HttpHeaders::getContentType).map(MediaType::toString)
					.orElse(null);
			if (contentType != null && contentType.contains("application/json")) {
				return DataBufferUtils.join(request.getBody()).map(dataBuffer -> {
					byte[] bytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(bytes);
					DataBufferUtils.release(dataBuffer);
					return bytes;
				}).defaultIfEmpty(new byte[0]).flatMap(bytes -> {
					DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
					ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
						@Override
						public Flux<DataBuffer> getBody() {
							if (bytes.length > 0) {
								return Flux.just(dataBufferFactory.wrap(bytes));
							}
							return Flux.empty();
						}
					};

					String bodyStr = new String(bytes, StandardCharsets.UTF_8);
					if (StringUtils.isNotBlank(bodyStr)) {
						bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(bodyStr));// 用json去掉有换行和空格
					}
					String finalBodyStr = bodyStr;

					traceManager.put(uniqueKey);
					log.info("{} {} {} header:{},param:{},body:{}", method, requestIp, path, headerStr, paramsStr,
							finalBodyStr);
					traceManager.remove();

					return chain.filter(exchange.mutate().request(decorator).build());
				});
			}

			String finalBodyStr = "{}";
			traceManager.put(uniqueKey);
			log.info("{} {} {} header:{},param:{},body:{}", method, requestIp, path, headerStr, paramsStr,
					finalBodyStr);
			traceManager.remove();
			return chain.filter(exchange.mutate().request(request).build());
		} catch (Exception e) {
			// 避免filter逻辑中的任何异常，直接转发请求
			log.error("{} {} {} header:{},filter error", method, requestIp, path, headerStr, e);
			return chain.filter(exchange);
		}
	}
}
