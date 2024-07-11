package com.company.order.retry;

import java.time.LocalDateTime;

import com.company.order.retry.method.CallableRetryMethod;
import com.company.order.retry.processor.Processor;
import com.company.order.retry.strategy.IncrementStrategy;
import com.company.order.retry.strategy.SecondsStrategy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetryTask {
	/**
	 * 执行方法
	 */
	@NonNull
	CallableRetryMethod method;

	/**
	 * 参数对象
	 */
	@NonNull
	Object params;

	/**
	 * 处理器
	 */
	Processor processor;

	/**
	 * 递增秒数
	 */
	@Builder.Default
	int increaseSeconds = 1;

	/**
	 * 最大失败次数
	 */
	@Builder.Default
	int maxFailure = 1;

	/**
	 * 当前失败次数
	 */
	@Builder.Default
	int failure = 0;

	/**
	 * 递增秒数策略
	 */
	@Builder.Default
	SecondsStrategy secondsStrategy = new IncrementStrategy();

	/**
	 * 指定首次的下次执行时间，可用于首次重试延迟
	 */
	@Builder.Default
	LocalDateTime nextDisposeTime = LocalDateTime.now();
}
