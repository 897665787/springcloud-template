package com.company.gateway.filter;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.company.common.util.MdcUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 日志记录响应body
 * 
 * @author Candi
 *
 */
@Slf4j
@Component
public class LogResponseBody implements RewriteFunction<String, String> {

	@Value("${logresponsebody.enable:true}")
	private boolean enable;
	
	@Override
	public Publisher<String> apply(ServerWebExchange exchange, String responseBody) {
		if (!enable) {
			return Mono.justOrEmpty(responseBody);
		}
		ServerHttpRequest request = exchange.getRequest();
		String uniqueKey = request.getHeaders().getFirst(MdcUtil.UNIQUE_KEY);
		MdcUtil.put(uniqueKey);
		log.info("response body:{}", responseBody);
		MdcUtil.remove();
		return Mono.justOrEmpty(responseBody);
	}
}