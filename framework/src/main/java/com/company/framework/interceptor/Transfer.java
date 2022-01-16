package com.company.framework.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.company.framework.aspect.IdempotentUtil;

import lombok.Data;

/**
 * http请求线程与Hystrix线程 传输对象
 */
@Data
public class Transfer {
	// 请求参数
	private RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	// 幂等ID
	private String idempotentId = IdempotentUtil.get();
	// 其他传递数据

	public void beforeCall() {
		RequestContextHolder.setRequestAttributes(requestAttributes);
		IdempotentUtil.set(idempotentId);
	}

	public void afterCall() {
		RequestContextHolder.resetRequestAttributes();
		IdempotentUtil.remove();
	}
}
