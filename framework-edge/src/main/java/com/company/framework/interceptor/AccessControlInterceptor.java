package com.company.framework.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;

public class AccessControlInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		String packageName = Optional.ofNullable(handlerMethod).map(HandlerMethod::getBeanType).map(Class::getPackage)
				.map(Package::getName).orElse(StringUtils.EMPTY);
		if (!packageName.startsWith("com.company")) {
			// springboot有些内置的Controller，不做拦截；只拦截业务代码的Controller
			return true;
		}

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
		
		return false;
	}
}
