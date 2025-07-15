package com.company.framework.feign;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.context.HttpContextUtil;
import com.google.common.collect.Maps;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用过程中传递header值
 */
@Component
public class HttpHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Map<String, Collection<String>> headers = Maps.newHashMap();
		// 请求上下文中传递到下游的相关headers
		headers.putAll(HttpContextUtil.httpContextHeaders());
		if (!headers.isEmpty()) {// 如果集合为空，template.headers会清空header
			template.headers(headers);
		}
	}
}
