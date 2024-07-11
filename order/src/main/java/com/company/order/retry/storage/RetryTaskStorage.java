package com.company.order.retry.storage;

import java.time.LocalDateTime;
import java.util.List;

import com.company.order.retry.RetryTask;

public interface RetryTaskStorage {
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param postParam post参数
	 * @return 成功/失败
	 */
	Boolean submit(RetryTask retryTask);

	/**
	 * 查询回调失败的回调ID
	 * 
	 * @return
	 */
	List<RetryTask> queryExecuteTask(int limit);

	void callbackSuccess(RetryTask retryTask, String message);

	void callbackFail(RetryTask retryTask, LocalDateTime nextDisposeTime, String message);

	void abandonCallback(RetryTask retryTask);
}