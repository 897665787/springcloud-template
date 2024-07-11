package com.company.order.retry.executor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;

import com.company.order.retry.RetryTask;
import com.company.order.retry.method.CallableRetryMethod;
import com.company.order.retry.method.RetryMethod;
import com.company.order.retry.method.RetryResult;
import com.company.order.retry.processor.AbandonRequestProcessor;
import com.company.order.retry.processor.BeforeRequestProcessor;
import com.company.order.retry.processor.Processor;
import com.company.order.retry.processor.ReturnSuccessProcessor;
import com.company.order.retry.storage.RetryTaskStorage;
import com.company.order.retry.strategy.SecondsStrategy;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadRetryTaskExecutor implements RetryTaskExecutor {

	RetryTaskStorage retryTaskStorage;

	public ThreadRetryTaskExecutor(RetryTaskStorage retryTaskStorage) {
		this.retryTaskStorage = retryTaskStorage;

		Thread thread = new Thread(() -> {
			int limit = 100;
			while (true) {
				log.info("execute task begin");
				List<RetryTask> executeTask = retryTaskStorage.queryExecuteTask(limit);
				for (RetryTask retryTask : executeTask) {
//					RetryTask<String> retryTask2 = RetryTask.<String>builder().failure(retryTask.getFailure()).build();
					execute(retryTask);
				}
				log.info("execute task end");
				if (executeTask.isEmpty()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public Boolean execute(RetryTask retryTask) {
		Object params = retryTask.getParams();

		Processor processor = retryTask.getProcessor();
		BeforeRequestProcessor beforeRequestProcessor = processor.beforeRequest();
		if (beforeRequestProcessor != null) {
			params = beforeRequestProcessor.beforeRequest(params);
		}

		CallableRetryMethod method = retryTask.getMethod();

		int failure = retryTask.getFailure();
		int maxFailure = retryTask.getMaxFailure();

		Callable<String> aa = () -> {
			int randomInt = RandomUtil.randomInt(10);
			System.out.println("randomInt:"+randomInt);
			if (randomInt < 9) {
				throw new RuntimeException("sddddddd:" + randomInt);
			}
			return "111111";
		};
		
		RetryResult retryResult = method.invoke(aa, params, failure, maxFailure);
//		RetryResult retryResult = retryMethod.invoke(method, params, failure, maxFailure);

		if (retryResult.isSuccess()) {
			retryTaskStorage.callbackSuccess(retryTask, retryResult.getMessage());

			ReturnSuccessProcessor returnSuccessProcessor = processor.returnSuccess();
			if (returnSuccessProcessor != null) {
				returnSuccessProcessor.afterReturnSuccess(params);
			}
			return null;
		}

		// 计算下次执行时间
		SecondsStrategy secondsStrategy = retryTask.getSecondsStrategy();
		int nextSeconds = secondsStrategy.nextSeconds(retryTask.getIncreaseSeconds(), retryTask.getFailure());
		LocalDateTime nextDisposeTime = retryTask.getNextDisposeTime().plusSeconds(nextSeconds);
		retryTaskStorage.callbackFail(retryTask, nextDisposeTime, retryResult.getMessage());

		if (retryResult.isAbandonCallback() || failure + 1 >= maxFailure) {
			retryTaskStorage.abandonCallback(retryTask);

			AbandonRequestProcessor abandonRequestProcessor = processor.abandonRequest();
			if (abandonRequestProcessor != null) {
				abandonRequestProcessor.afterAbandonRequest(params, retryResult.getMessage());
			}
		}

		return null;
	}
}
