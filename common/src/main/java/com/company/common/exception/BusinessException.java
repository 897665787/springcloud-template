package com.company.common.exception;

import com.company.common.api.ResultCode;

/**
 * 业务异常
 * 
 * @author jiangqingdi
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;

	public BusinessException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public BusinessException(String message) {
		this.code = ResultCode.PARAM_INVALID.getCode();
		this.message = message;
	}

	public BusinessException(String message, Throwable e) {
		super(message, e);
		this.message = message;
	}

	public static BusinessException of(ResultCode resultCode) {
		return new BusinessException(resultCode.getCode(), resultCode.getMessage());
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
