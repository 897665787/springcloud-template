package com.company.gateway.constant;

public interface CommonConstants {
	String BASE_PACKAGE = "com.company";

	/**
	 * 过滤器优先级
	 */
	public interface FilterOrdered {
		// 值越小，优先级越高
		int MDC = -10;
		int RESPONSE = -1;// 这个过滤器值必须小于0，否则不起作用
		int REQUEST = 10;
		int SQLINJECT = 20;
	}
}
