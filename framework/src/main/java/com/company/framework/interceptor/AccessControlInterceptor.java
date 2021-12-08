package com.company.framework.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;

@Component
public class AccessControlInterceptor extends HandlerInterceptorAdapter {
	
	@Value("${template.enable.access-control:true}")
	private Boolean enable;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(!enable){
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 获取方法上的注解
		Method method = handlerMethod.getMethod();
		if (method.isAnnotationPresent(PublicUrl.class)) {
			return true;
		}
		
		// 获取类上的注解
		Object bean = handlerMethod.getBean();
		if (bean.getClass().isAnnotationPresent(PublicUrl.class)) {
			return true;
		}
		
		// 判断是否已登录
		String token = request.getHeader("token");
		if ("111".equals(token)) {
			return true;
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		Result fail = Result.fail("请登录");
		writer.write(JsonUtil.toJsonString(fail));
		
		return false;
	}
}
