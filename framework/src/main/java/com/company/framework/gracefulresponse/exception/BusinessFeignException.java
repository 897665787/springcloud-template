package com.company.framework.gracefulresponse.exception;

import com.company.common.api.ResultCode;
import com.feiniaojin.gracefulresponse.api.ExceptionMapper;
import feign.FeignException;

/**
 * 业务异常
 *
 * @author JQ棣
 */
@ExceptionMapper(code = "32121", msg = "业务异常aaaaaaaaa", msgReplaceable = true)
public class BusinessFeignException extends FeignException {
	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;

	public BusinessFeignException(Integer code, String message) {
        super(code, message);
        this.code = code;
		this.message = message;
	}

	public BusinessFeignException(String message) {
        super(ResultCode.PARAM_INVALID.getCode(), message);
        this.code = ResultCode.PARAM_INVALID.getCode();
		this.message = message;
	}

	public BusinessFeignException(String message, Throwable e) {
        super(ResultCode.PARAM_INVALID.getCode(), message, e);
        this.message = message;
	}

	public static BusinessFeignException of(ResultCode resultCode) {
		return new BusinessFeignException(resultCode.getCode(), resultCode.getMessage());
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
