package com.company.order.retry.method;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetryResult {
	/**
	 * 调用成功/失败
	 */
	boolean success;

	/**
	 * 信息（success=false有用）
	 */
	String message;

	/**
	 * 放弃重试（success=false有用）
	 */
	boolean abandonCallback;
}
