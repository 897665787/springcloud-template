package com.company.gateway.filter;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.company.common.util.JsonUtil;
import com.company.gateway.util.IpUtil;

import io.netty.buffer.ByteBufAllocator;
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

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		long start = System.currentTimeMillis();
		String contentType = Optional.ofNullable(headers).map(HttpHeaders::getContentType).map(MediaType::toString)
				.orElse(null);
		String bodyStr = "{}";
		if (contentType != null && contentType.contains("application/json")) {
			bodyStr = resolveBodyFromRequest(request);// 用json去掉有换行和空格

			DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
			Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

			request = new ServerHttpRequestDecorator(request) {
				@Override
				public Flux<DataBuffer> getBody() {
					return bodyFlux;
				}
			};

			bodyStr = JsonUtil.toJsonString(JsonUtil.readTree(bodyStr));// 用json去掉有换行和空格
		}

		String paramsStr = JsonUtil.toJsonString(getReqParam(request));
		String requestIp = IpUtil.getRequestIp(request);
		String headerStr = JsonUtil.toJsonString(headers.toSingleValueMap());

		log.info("{} {} {} header:{},param:{},body:{}", request.getMethod(), requestIp, request.getURI().getPath(),
				headerStr, paramsStr, bodyStr);

		Mono<Void> mono = chain.filter(exchange.mutate().request(request).build());

		log.info("{} {} {} header:{},param:{},body:{},{}ms", request.getMethod(), requestIp, request.getURI().getPath(),
				headerStr, paramsStr, bodyStr, System.currentTimeMillis() - start);

		return mono;
	}

	/**
	 * 从Flux<DataBuffer>中获取字符串的方法
	 * 
	 * @return 请求体
	 */
	private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
		// 获取请求体
		Flux<DataBuffer> body = serverHttpRequest.getBody();

		AtomicReference<String> bodyRef = new AtomicReference<>();
		body.subscribe(buffer -> {
			CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
			DataBufferUtils.release(buffer);
			bodyRef.set(charBuffer.toString());
		});
		// 获取request body
		return bodyRef.get();
	}

	private DataBuffer stringBuffer(String value) {
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

		NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
		DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
		buffer.write(bytes);
		return buffer;
	}

	/**
	 * 组装request中的参数
	 * 
	 * <pre>
	 * 以下场景都能通过request.getParameterNames获取参数
	 * 1.参数跟在url后面
	 * 2.POST form-data
	 * 3.POST x-www-form-urlencoded
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getReqParam(ServerHttpRequest request) {
		MultiValueMap<String, String> queryParams = request.getQueryParams();
		Map<String, String> paramMap = queryParams.toSingleValueMap();
		return paramMap;
	}
}
