package com.company.framework.advice;

import com.company.common.api.Result;
import com.company.framework.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class UnauthorizedExceptionHandler {

	/**
	 * 未授权异常
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public Result<?> unauthorized(UnauthorizedException e, HttpServletRequest request, HttpServletResponse response,
								  HandlerMethod handler) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		sendErrorIfPage(request, response, handler);
		return Result.fail(e);
	}

	private boolean isReturnJson(HandlerMethod handler) {
		if (handler.getBeanType().isAnnotationPresent(RestController.class)) {
			return true;
		}
		Method method = handler.getMethod();
		if (method.isAnnotationPresent(ResponseBody.class)) {
			return true;
		}
		return false;
	}

	private void sendErrorIfPage(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		String accept = request.getHeader("Accept");
		if (StringUtils.isNotBlank(accept) && accept.contains("application/json")) {
			return;
		}
		String contentType = response.getContentType();
		if (StringUtils.isNotBlank(contentType) && contentType.contains("application/json")) {
			return;
		}
		if (isReturnJson(handler)) {
			return;
		}
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("sendError error", e);
		}
	}
}
