package com.company.gateway.filter;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
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

import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.gateway.util.IpUtil;
import com.company.gateway.util.WebUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@Order(10)
public class RequestFilter implements GlobalFilter {

	@Value("${filter.request.enable:true}")
	private boolean enable;

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
		long start = System.currentTimeMillis();
		try {
			String paramsStr = JsonUtil.toJsonString(WebUtil.getReqParam(request));
			String uniqueKey = request.getHeaders().getFirst(MdcUtil.UNIQUE_KEY);

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

					String bodyStr = new String(bytes);
					if (StringUtils.isNotBlank(bodyStr)) {
						bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(bodyStr));// 用json去掉有换行和空格
					}
					String finalBodyStr = bodyStr;

					return chain.filter(exchange.mutate().request(decorator).build()).then(Mono.fromRunnable(() -> {
						MdcUtil.put(uniqueKey);
						log.info("{} {} {} header:{},param:{},body:{},{}ms", method, requestIp, path, headerStr,
								paramsStr, finalBodyStr, System.currentTimeMillis() - start);
						MdcUtil.remove();
					}));
				});
			}

			String finalBodyStr = "{}";
			return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
				MdcUtil.put(uniqueKey);
				log.info("{} {} {} header:{},param:{},body:{},{}ms", method, requestIp, path, headerStr, paramsStr,
						finalBodyStr, System.currentTimeMillis() - start);
				MdcUtil.remove();
			}));
		} catch (Exception e) {
			// 避免filter逻辑中的任何异常，直接转发请求
			log.error("{} {} {} header:{},{}ms,filter error", method, requestIp, path, headerStr,
					System.currentTimeMillis() - start, e);
			return chain.filter(exchange);
		}
	}
}
