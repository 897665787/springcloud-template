package com.company.order.retry.storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.company.order.retry.RetryTask;
import com.google.common.collect.Lists;

public class CacheRetryStorage implements RetryTaskStorage {
	ArrayBlockingQueue<RetryTask> queue = new ArrayBlockingQueue<>(1000);
	
	@Override
	public Boolean submit(RetryTask retryTask) {
		return queue.offer(retryTask);
	}

	@Override
	public List<RetryTask> queryExecuteTask(int limit) {
		List<RetryTask> subList = Lists.newArrayList();
		for (int i = 0; i < limit; i++) {
			RetryTask retryTask = queue.poll();
			if (retryTask == null) {
				break;
			}
			LocalDateTime nextDisposeTime = retryTask.getNextDisposeTime();
			if (nextDisposeTime.compareTo(LocalDateTime.now()) <= 0) {
				subList.add(retryTask);
			} else {
				queue.offer(retryTask);
			}
		}
		return subList;
	}

	@Override
	public void callbackSuccess(RetryTask retryTask, String message) {
		// TODO Auto-generated method stub
		System.out.println("CacheRetryStorage callbackSuccess");
	}

	@Override
	public void callbackFail(RetryTask retryTask, LocalDateTime nextDisposeTime, String message) {
		// TODO Auto-generated method stub
		System.out.println("CacheRetryStorage callbackFail");
		retryTask.setFailure(retryTask.getFailure() + 1);
		retryTask.setNextDisposeTime(nextDisposeTime);
		queue.offer(retryTask);
	}

	@Override
	public void abandonCallback(RetryTask retryTask) {
		// TODO Auto-generated method stub
		System.out.println("CacheRetryStorage abandonCallback");
	}

}
