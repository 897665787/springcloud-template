package com.company.framework.advice;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.common.exception.BusinessException;
import com.company.common.util.MdcUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 拦截异常
	 */
	@ExceptionHandler(Exception.class)
	public Result<?> error(Exception e) {
		log.error("未知异常:", e);
		return Result.fail(ResultCode.SYSTEM_ERROR).setTraceId(MdcUtil.get());
	}
	
	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		String message = MessageFormat.format("不支持{0}请求", e.getMethod());
		log.warn(message, e);
		return Result.fail(message);
	}
	
	/**
	 * 拦截未处理的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public Result<?> runtime(RuntimeException e) {
		log.error("未处理运行时异常", e);
		return Result.fail(ResultCode.SYSTEM_ERROR).setTraceId(MdcUtil.get());
	}
	
	/**
	 * 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public Result<?> business(BusinessException e) {
		String message = e.getMessage();
		if (StringUtils.isBlank(message)) {
			message = ExceptionUtils.getStackTrace(e);
		}
		log.warn("业务异常:{}", message);
		return Result.fail(e);
	}
	
	// 各种运行时异常单独处理可以在这里添加,例如
	/**
	 * 用来处理bean validation异常
	 * 主要是 resolveMethodArgumentNotValidException，handleBingException 这两个方法
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public Result<?> resolveConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			String messageStr = constraintViolations.stream().map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(","));
			return Result.fail(messageStr);
		}
		return Result.fail(ex.getMessage());
	}

	/**
	 * @Desc 处理 @RequestBody 类型的 POJO 参数
	 **/
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<?> resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
		if (!CollectionUtils.isEmpty(objectErrors)) {
			String messageStr = objectErrors.stream().map(ObjectError::getDefaultMessage)
					.collect(Collectors.joining(","));
			return Result.fail(messageStr);
		}
		return Result.fail(ex.getMessage());
	}

	/**
	 * @Desc 处理表单提交的 pojo 参数
	 **/
	@ExceptionHandler(BindException.class)
	public Result<?> handleBingException(BindException e) {
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		if (!CollectionUtils.isEmpty(allErrors)) {
			String messageStr = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
			return Result.fail(messageStr);
		}
		return Result.fail( e.getMessage());
	}

}
