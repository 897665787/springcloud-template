package com.company.gateway.developer.policy.filter;

import com.company.common.constant.CommonConstants;
import com.company.gateway.util.TokenValueUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 将token放入上下文
 */
@Component
public class TokenFilter implements GlobalFilter, Ordered {

	@Value("${token.name}")
	private String headerToken;

	@Value("${token.prefix:}")
	private String tokenPrefix;

	@Override
	public int getOrder() {
		return CommonConstants.FilterOrdered.TOKEN;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String token = request.getHeaders().getFirst(headerToken);
		token = TokenValueUtil.fixToken(tokenPrefix, token);

		TokenContext.setToken(token);

		return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
			TokenContext.removeToken();
		}));
	}
}
