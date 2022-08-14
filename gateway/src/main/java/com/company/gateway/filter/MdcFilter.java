package com.company.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.company.common.util.MdcUtil;

import reactor.core.publisher.Mono;

/**
 * 对客户端请求添加MDC
 */
@Component
@Order(1)
public class MdcFilter implements GlobalFilter {

	static {
		System.setProperty("log4j2.isThreadContextMapInheritable", "true");
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		MdcUtil.put();
		request = request.mutate().header(MdcUtil.UNIQUE_KEY, MdcUtil.get()).build();
		try {
			// 把新的 exchange放回到过滤链
	        return chain.filter(exchange.mutate().request(request).build());
		} finally {
//			MdcUtil.remove();// 这里执行了日志ID就没了
		}
	}
}
