package com.company.framework.interceptor;

import org.springframework.stereotype.Component;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.MdcUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用过程中传递header值
 * 
 * 依赖Hystrix自定义并发策略:RequestAttributeHystrixConcurrencyStrategy
 */
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		// 当前登录用户相关headers
		template.headers(HttpContextUtil.currentUserHeaders());
		// 日志追踪ID
		template.headers(MdcUtil.headers());
		// 其他headers
		// template.headers(headers);
	}
}
