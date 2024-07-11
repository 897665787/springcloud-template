package com.company.order.retry;

import java.util.Map;

import com.company.order.retry.executor.ThreadRetryTaskExecutor;
import com.company.order.retry.method.CallableRetryMethod;
import com.company.order.retry.processor.FunctionProcessor;
import com.company.order.retry.processor.Processor;
import com.company.order.retry.storage.CacheRetryStorage;
import com.company.order.retry.storage.RetryTaskStorage;
import com.google.common.collect.Maps;

public class Test {
	public static void main(String[] args) {
		Map<String,String> params = Maps.newHashMap();
		params.put("id", "1");
		
		CallableRetryMethod retryMethod = new CallableRetryMethod();

		Processor processor = new FunctionProcessor();
		
		RetryTask retryTask = RetryTask.builder()
				.method(retryMethod).params(params)
				.processor(processor)
				.increaseSeconds(2)
				.maxFailure(5)
				.build();

		RetryTaskStorage retryTaskStorage = new CacheRetryStorage();
		
//		RetryTaskExecutor<String> taskExecutor = 
				new ThreadRetryTaskExecutor(retryTaskStorage);
		
		retryTaskStorage.submit(retryTask);
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
