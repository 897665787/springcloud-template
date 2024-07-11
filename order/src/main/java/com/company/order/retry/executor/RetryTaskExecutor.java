package com.company.order.retry.executor;

import com.company.order.retry.RetryTask;

public interface RetryTaskExecutor {
	Boolean execute(RetryTask retryTask);
}
