package com.company.framework.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.common.util.JsonUtil;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessControlInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		Method method = handlerMethod.getMethod();
		
		if (!method.isAnnotationPresent(RequireLogin.class)
				&& !method.getDeclaringClass().isAnnotationPresent(RequireLogin.class)) {
			// 方法和类上都没有打注解RequireLogin
			return true;
		}
		
		// 判断是否已登录
//		String userId = request.getHeader(HttpContextUtil.HEADER_CURRENT_USER_ID);
		// 注：为了防止直接在header设置用户ID，绕过认证，要取最后1个值
		Enumeration<String> headerCurrentUserIdEnum = request.getHeaders(HttpContextUtil.HEADER_CURRENT_USER_ID);
		String lastCurrentUserId = null;
		while (headerCurrentUserIdEnum.hasMoreElements()) {
			lastCurrentUserId = headerCurrentUserIdEnum.nextElement();
		}
		
		String userId = lastCurrentUserId;
		if (StringUtils.isNotBlank(userId)) {
			return true;
		}
		
		// 判断是否有访问权限？
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		Result<?> fail = Result.fail(ResultCode.NO_LOGIN);
		writer.write(JsonUtil.toJsonString(fail));
		log.warn("登录过期:{}.{}", method.getDeclaringClass().getName(), method.getName());
		
		return false;
	}
}
