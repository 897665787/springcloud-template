package com.company.common.exception;

import com.company.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务异常
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

	public static BusinessException of(ResultCode resultCode) {
		return new BusinessException(resultCode.getCode(), resultCode.getMessage());
	}
}
