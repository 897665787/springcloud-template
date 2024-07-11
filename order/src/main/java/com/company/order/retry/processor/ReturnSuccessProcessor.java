package com.company.order.retry.processor;

/**
 * 返回成功
 * 
 * @author Candi
 *
 */
public interface ReturnSuccessProcessor {

	/**
	 * 返回成功之后
	 * 
	 * @return
	 */
	void afterReturnSuccess(Object params);
}
