package com.company.framework.gracefulresponse.exception;

import com.company.common.api.ResultCode;
import com.feiniaojin.gracefulresponse.api.ExceptionMapper;

/**
 * 业务异常
 *
 * @author JQ棣
 */
@ExceptionMapper(code= "3212", msg = "业务异常", msgReplaceable = true)
public class BusinessGRException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;

	public BusinessGRException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public BusinessGRException(String message) {
		this.code = ResultCode.PARAM_INVALID.getCode();
		this.message = message;
	}

	public BusinessGRException(String message, Throwable e) {
		super(message, e);
		this.message = message;
	}

	public static BusinessGRException of(ResultCode resultCode) {
		return new BusinessGRException(resultCode.getCode(), resultCode.getMessage());
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
