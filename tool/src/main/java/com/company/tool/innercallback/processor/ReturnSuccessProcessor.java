package com.company.tool.innercallback.processor;

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
	void afterReturnSuccess(String jsonParams);
}
