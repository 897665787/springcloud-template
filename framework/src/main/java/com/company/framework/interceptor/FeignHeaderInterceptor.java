package com.company.framework.interceptor;

import org.springframework.stereotype.Component;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.MdcUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign调用过程中传递header值
 * Hystrix 提供两种执行隔离策略(SEMAPHORE ：信号量，命令在调用线程执行,THREAD ：线程池，命令在线程池执行), 默认配置的为THREAD，因为不在同一个线程中，无法获得请求的上下文对象
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
