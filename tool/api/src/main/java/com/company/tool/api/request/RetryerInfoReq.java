package com.company.tool.api.request;

import java.time.LocalDateTime;

import com.company.tool.api.enums.RetryerEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetryerInfoReq {
	/**
	 * feign地址
	 */
	@NonNull
	String feignUrl;
	
	/**
	 * 可转化为JSON的参数对象
	 */
	@NonNull
	Object jsonParams;
	
	/**
	 * 递增秒数
	 */
	@Builder.Default
	int increaseSeconds = 1;
	
	/**
	 * 最大失败次数
	 */
	@Builder.Default
	int maxFailure = 3;
	
	/**
	 * 递增秒数策略
	 */
	@Builder.Default
	RetryerEnum.SecondsStrategy secondsStrategy = RetryerEnum.SecondsStrategy.INCREMENT;
	
	/**
	 * 指定首次的下次执行时间，可用于首次重试延迟
	 */
	@Builder.Default
	LocalDateTime nextDisposeTime = LocalDateTime.now();
}