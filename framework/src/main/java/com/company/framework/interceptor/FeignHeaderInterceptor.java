package com.company.framework.interceptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.company.framework.trace.TraceManager;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.aspect.IdempotentUtil;
import com.company.framework.cache.ICache;
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

	@Autowired
	private ICache cache;
	@Autowired
	private TraceManager traceManager;

	@Override
	public void apply(RequestTemplate template) {
		Map<String, Collection<String>> headers = Maps.newHashMap();

		// 请求上下文中传递到下游的相关headers
		headers.putAll(HttpContextUtil.httpContextHeaders());
		// 日志追踪ID
		headers.putAll(traceManager.headers());
		// 幂等
		headers.putAll(idempotentheaders(IdempotentUtil.headers()));
		// 其他headers
		// headers.putAll();
		if (!headers.isEmpty()) {// 如果集合为空，template.headers会清空header
			template.headers(headers);
		}
	}

	public Map<String, Collection<String>> idempotentheaders(Map<String, String> idempotentheaders) {
		if (MapUtils.isEmpty(idempotentheaders)) {
			return Collections.emptyMap();
		}

		HashMap<String, Collection<String>> headers = Maps.newHashMap();
		headers.put(IdempotentUtil.HEADER_IDEMPOTENT_ID,
				Arrays.asList(idempotentheaders.get(IdempotentUtil.HEADER_IDEMPOTENT_ID)));
		String expireMillis = idempotentheaders.get(IdempotentUtil.HEADER_IDEMPOTENT_EXPIRE_MILLIS);
		headers.put(IdempotentUtil.HEADER_IDEMPOTENT_EXPIRE_MILLIS, Arrays.asList(expireMillis));

		cache.set(IdempotentUtil.head(IdempotentUtil.HEADER_IDEMPOTENT_ID), StringUtils.EMPTY,
				Integer.parseInt(expireMillis), TimeUnit.MILLISECONDS);
		return headers;
	}
}
