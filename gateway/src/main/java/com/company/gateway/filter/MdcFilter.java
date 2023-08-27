package com.company.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.company.common.constant.CommonConstants;
import com.company.common.util.MdcUtil;

import reactor.core.publisher.Mono;

/**
 * 对客户端请求添加MDC
 */
@Component
public class MdcFilter implements GlobalFilter, Ordered {

	static {
		System.setProperty("log4j2.isThreadContextMapInheritable", "true");
	}

	@Override
	public int getOrder() {
		return CommonConstants.FilterOrdered.MDC;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		MdcUtil.put();
		request = request.mutate().header(MdcUtil.UNIQUE_KEY, MdcUtil.get()).build();

		return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
			MdcUtil.remove();
		}));
	}
}
