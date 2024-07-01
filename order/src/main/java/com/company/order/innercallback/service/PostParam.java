package com.company.order.innercallback.service;

import java.time.LocalDateTime;

import com.company.order.enums.InnerCallbackEnum;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostParam {
	/**
	 * 回调地址
	 */
	@NonNull
	String notifyUrl;
	
	/**
	 * 可转化为JSON的参数对象
	 */
	@NonNull
	Object jsonParams;
	
	/**
	 * 处理器bean名称
	 */
	ProcessorBeanName processorBeanName;
	
	/**
	 * 递增秒数
	 */
	@Builder.Default
	int increaseSeconds = 0;
	
	/**
	 * 最大失败次数
	 */
	@Builder.Default
	int maxFailure = 0;
	
	/**
	 * 递增秒数策略
	 */
	@Builder.Default
	InnerCallbackEnum.SecondsStrategy secondsStrategy = InnerCallbackEnum.SecondsStrategy.INCREMENT;
	
	/**
	 * 指定首次的下次执行时间，可用于首次重试延迟
	 */
	@Builder.Default
	LocalDateTime nextDisposeTime = LocalDateTime.now();
}