package com.company.framework.feign;

import com.company.framework.context.HeaderContextUtil;
import com.google.common.collect.Maps;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * feign调用过程中传递header值
 */
@Component
public class HttpHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Map<String, Collection<String>> headers = Maps.newHashMap();
		// 请求上下文中传递到下游的相关headers
		headers.putAll(HeaderContextUtil.httpContextHeaders());
		if (!headers.isEmpty()) {// 如果集合为空，template.headers会清空header
			template.headers(headers);
		}
	}
}
