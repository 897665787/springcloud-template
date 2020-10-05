package com.company.common.api;

import com.company.common.exception.BusinessException;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Result {
	private Integer code;// 响应码
	private String message;// 响应信息
	private Object data;// 数据

//	public Result of(Integer code, String message) {
//		this.code = code;
//		this.message = message;
//		return this;
//	}
	
	public Result setResultCode(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
		return this;
	}

	public static Result success() {
		return new Result().setResultCode(ResultCode.SUCCESS);
	}

	public static Result success(Object data) {
		return new Result().setResultCode(ResultCode.SUCCESS).setData(data);
	}

	public static Result fail() {
		return new Result().setResultCode(ResultCode.FAIL);
	}

	public static Result fail(String message) {
		return new Result().setResultCode(ResultCode.FAIL).setMessage(message);
	}

	public static Result fail(Object data) {
		return new Result().setResultCode(ResultCode.FAIL).setData(data);
	}

	public static Result fail(ResultCode resultCode) {
		return new Result().setResultCode(resultCode);
	}

	public static Result fail(ResultCode resultCode, Object data) {
		return new Result().setResultCode(resultCode).setData(data);
	}

	public static Result fail(BusinessException businessException) {
		return new Result().setCode(businessException.getCode()).setMessage(businessException.getMessage());
	}
}
