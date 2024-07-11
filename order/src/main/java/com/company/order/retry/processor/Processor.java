package com.company.order.retry.processor;

public interface Processor {
	/**
	 * 处理之前
	 */
	BeforeRequestProcessor beforeRequest();

	/**
	 * 响应成功
	 */
	ReturnSuccessProcessor returnSuccess();

	/**
	 * 放弃请求
	 */
	AbandonRequestProcessor abandonRequest();
}
