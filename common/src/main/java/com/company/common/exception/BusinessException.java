package com.company.common.exception;

import com.company.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务异常（建议交由全局异常处理器 GlobalExceptionHandler 处理抛出该异常，否则直接抛出RuntimeException或者对应含义的异常就好）
 *
 * @author JQ棣
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	private Integer code;

	public BusinessException(Integer code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String message) {
		super(message);
		this.code = ResultCode.PARAM_INVALID.getCode();
	}

	public BusinessException(String message, Throwable e) {
		super(message, e);
		this.code = ResultCode.PARAM_INVALID.getCode();
	}
}
