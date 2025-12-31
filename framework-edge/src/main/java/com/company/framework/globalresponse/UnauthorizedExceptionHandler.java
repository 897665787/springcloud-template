package com.company.framework.globalresponse;


import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.framework.message.IMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */
@Slf4j
//@RestControllerAdvice
@Deprecated // 替换为GracefulResponse框架处理
@RequiredArgsConstructor
public class UnauthorizedExceptionHandler {
	private final IMessage imessage;

	/**
	 * 未授权异常
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public Result<?> unauthorized(UnauthorizedException e, HttpServletRequest request, HttpServletResponse response,
                                  HandlerMethod handler) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// 可根据前端需求看是否使用http状态码来表示未登录，如果要使用状态码200，则注释掉该行代码
		ResultCode resultCode = ResultCode.UNAUTHORIZED;
		return Result.fail(resultCode.getCode(), imessage.getMessage(resultCode.getMessage()));
	}
}
