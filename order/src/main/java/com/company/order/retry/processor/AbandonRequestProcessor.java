package com.company.order.retry.processor;

/**
 * 放弃请求处理器
 * 
 * @author Candi
 *
 */
public interface AbandonRequestProcessor {
	/**
	 * 放弃请求之后
	 * 
	 * @return
	 */
	void afterAbandonRequest(Object params, String abandonReason);
}
