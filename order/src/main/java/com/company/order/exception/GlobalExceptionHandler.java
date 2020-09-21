package com.company.order.exception;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.company.common.api.Result;
import com.company.common.exception.BusinessException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//	/**
//	 * 密码错误
//	 */
//	@ExceptionHandler(IncorrectCredentialsException.class)
//	public Object incorrectCredentialsException(HttpServletRequest request, IncorrectCredentialsException e) {
//		logger.warn("密码错误", e);
//		return Result.fail("密码错误");
//	}
//
//	/**
//	 * 权限校验失败，如果请求为ajax返回json，普通请求跳转页面
//	 */
//	@ExceptionHandler(AuthorizationException.class)
//	public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e) {
//		// request.getRequestURI();
//		logger.warn("未授权访问", e);
//		if (ServletUtils.isAjaxRequest(request)) {
//			return Result.fail(ExceptionUtils.getStackTrace(e));
//		} else {
//			ModelAndView modelAndView = new ModelAndView();
//			modelAndView.setViewName("unauth");
//			return modelAndView;
//		}
//	}

	/**
	 * 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public Object handleBusinessException(HttpServletRequest request, BusinessException e) {
		logger.warn("业务异常:{}", e.getMessage());
		return Result.fail(ExceptionUtils.getStackTrace(e));
		// if (ServletUtils.isAjaxRequest(request)) {
		// return Result.fail(ExceptionUtils.getStackTrace(e));
		// } else {
		// ModelAndView modelAndView = new ModelAndView();
		// modelAndView.addObject("message", ExceptionUtils.getStackTrace(e));
		// modelAndView.setViewName("error");
		// return modelAndView;
		// }
	}

	//
	// /**
	// * 演示模式异常
	// */
	// @ExceptionHandler(DemoModeException.class)
	// public Result demoModeException(DemoModeException e) {
	// return Result.fail("演示模式，不允许操作");
	// }

	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		String message = MessageFormat.format("不支持{0}请求", e.getMethod());
		logger.warn(message, e);
		return Result.fail(message);
	}

	/**
	 * 拦截未处理的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public Result handleRuntimeException(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
		logger.error("未处理运行时异常", e);
		return Result.fail("未处理运行时异常:" + ExceptionUtils.getStackTrace(e));
	}

	/**
	 * 系统异常
	 */
	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e) {
		logger.error("系统异常，请联系管理员", e);
		return Result.fail("系统异常，请联系管理员");
	}
}
