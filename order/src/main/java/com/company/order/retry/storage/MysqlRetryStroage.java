package com.company.order.retry.storage;

import java.time.LocalDateTime;
import java.util.List;

import com.company.order.retry.RetryTask;

public class MysqlRetryStroage implements RetryTaskStorage {

	@Override
	public Boolean submit(RetryTask retryTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RetryTask> queryExecuteTask(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void callbackSuccess(RetryTask retryTask, String message) {
		// TODO Auto-generated method stub
		System.out.println("MysqlRetryStroage callbackSuccess");
	}

	@Override
	public void callbackFail(RetryTask retryTask, LocalDateTime nextDisposeTime, String message) {
		// TODO Auto-generated method stub
		System.out.println("MysqlRetryStroage callbackFail");
		retryTask.setFailure(retryTask.getFailure() + 1);
		retryTask.setNextDisposeTime(nextDisposeTime);
	}

	@Override
	public void abandonCallback(RetryTask retryTask) {
		// TODO Auto-generated method stub
		System.out.println("MysqlRetryStroage abandonCallback");
	}

}
