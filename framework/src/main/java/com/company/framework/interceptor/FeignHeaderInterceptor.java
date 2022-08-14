package com.company.framework.interceptor;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.common.util.MdcUtil;
import com.company.framework.aspect.IdempotentUtil;
import com.company.framework.context.HttpContextUtil;
import com.google.common.collect.Maps;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用过程中传递header值
 * 
 * 依赖Hystrix自定义并发策略:TransferHystrixConcurrencyStrategy
 */
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Map<String, Collection<String>> headers = Maps.newHashMap();

		// 请求上下文中传递到下游的相关headers
		headers.putAll(HttpContextUtil.httpContextHeaders());
		// 日志追踪ID
		headers.putAll(MdcUtil.headers());
		// 幂等
		headers.putAll(IdempotentUtil.headers());
		// 其他headers
		// headers.putAll();
		if (!headers.isEmpty()) {// 如果集合为空，template.headers会清空header
			template.headers(headers);
		}
	}
}
