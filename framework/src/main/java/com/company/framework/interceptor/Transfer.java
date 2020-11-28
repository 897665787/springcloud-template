package com.company.framework.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * http请求线程与Hystrix线程 传输对象
 */
public class Transfer {
	// 请求参数
	private RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	// MDC日志追踪ID
//	private String traceId = MdcUtil.get();
	// 其他传递数据

	public void beforeCall() {
		RequestContextHolder.setRequestAttributes(requestAttributes);
//		MdcUtil.put(traceId);
	}

	public void afterCall() {
		RequestContextHolder.resetRequestAttributes();
//		MdcUtil.remove();
	}
}
