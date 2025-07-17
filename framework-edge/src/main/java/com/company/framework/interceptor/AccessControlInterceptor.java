package com.company.framework.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.framework.annotation.RequireLogin;
import com.company.framework.constant.HeaderConstants;
import com.company.framework.globalresponse.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "access-control", havingValue = "true", matchIfMissing = true)
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		Method method = handlerMethod.getMethod();

		RequireLogin methodAnnotation = AnnotationUtils.findAnnotation(method, RequireLogin.class);
		RequireLogin classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RequireLogin.class);
		if (methodAnnotation == null && classAnnotation == null) {
			// 方法和类上都没有打注解RequireLogin
			return true;
		}

		// 判断是否已登录
//		String userId = request.getHeader(HeaderConstants.HEADER_CURRENT_USER_ID);
		// 注：为了防止直接在header设置用户ID，绕过认证，要取最后1个值
		Enumeration<String> headerCurrentUserIdEnum = request.getHeaders(HeaderConstants.HEADER_CURRENT_USER_ID);
		String lastCurrentUserId = null;
		while (headerCurrentUserIdEnum.hasMoreElements()) {
			lastCurrentUserId = headerCurrentUserIdEnum.nextElement();
		}

		String userId = lastCurrentUserId;
		if (StringUtils.isNotBlank(userId)) {
			return true;
		}

		// 判断是否有访问权限？
		log.warn("访问未授权:{}.{}", method.getDeclaringClass().getName(), method.getName());
		throw new UnauthorizedException();
	}

}
