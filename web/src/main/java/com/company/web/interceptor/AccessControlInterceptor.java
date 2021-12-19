package com.company.web.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;

public class AccessControlInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 获取方法上的注解
		Method method = handlerMethod.getMethod();
		if (method.isAnnotationPresent(PublicUrl.class)) {
			return true;
		}
		
		// 获取类上的注解
		if (method.getDeclaringClass().isAnnotationPresent(PublicUrl.class)) {
			return true;
		}
		
		// 判断是否已登录
		String userId = request.getHeader(HttpContextUtil.HEADER_CURRENT_USER_ID);
		if (StringUtils.isNotBlank(userId)) {
			return true;
		}
		
		// 判断是否有访问权限？
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		Result fail = Result.fail("请登录");
		writer.write(JsonUtil.toJsonString(fail));
		
		return false;
	}
}
