package com.company.gateway.developer.policy.context;

import com.company.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取请求头中的开发者ID，并将其存入上下文中，供后续使用。
 */
public class DeveloperFilter implements GlobalFilter, Ordered {

    @Value("${token.name}")
    private String headerToken;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(headerToken);

        DeveloperContext.set(token);

        return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
            DeveloperContext.remove();
        }));
    }
}
