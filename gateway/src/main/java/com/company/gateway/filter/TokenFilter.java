package com.company.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.company.common.constant.HeaderConstants;
import com.company.gateway.token.TokenService;

import reactor.core.publisher.Mono;

/**
 * token解析，把token转换为USER_ID
 */
@Component
@Order(30)
public class TokenFilter implements GlobalFilter {

	@Autowired
	private TokenService tokenService;
	
	@Value("${token.name}")
	private String headerToken;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String token = headers.getFirst(headerToken);
		if (StringUtils.isBlank(token)) {
			// 注：为了防止直接在header设置用户ID，绕过认证，要设置用户ID为空
			request = request.mutate().header(HeaderConstants.HEADER_CURRENT_USER_ID, StringUtils.EMPTY).build();
			return chain.filter(exchange.mutate().request(request).build());
		}
		
		String userId = tokenService.checkAndGet(token);
		if (StringUtils.isBlank(userId)) {
			// 注：为了防止直接在header设置用户ID，绕过认证，要设置用户ID为空
			request = request.mutate().header(HeaderConstants.HEADER_CURRENT_USER_ID, StringUtils.EMPTY).build();
			return chain.filter(exchange.mutate().request(request).build());
		}

		request = request.mutate().header(HeaderConstants.HEADER_CURRENT_USER_ID, userId).build();
		return chain.filter(exchange.mutate().request(request).build());
	}
}