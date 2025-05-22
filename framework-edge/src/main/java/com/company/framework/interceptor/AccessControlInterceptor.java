package com.company.framework.interceptor;

import com.company.common.api.ResultCode;
import com.company.common.exception.BusinessException;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;

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
		log.warn("登录过期:{}.{}", method.getDeclaringClass().getName(), method.getName());
		throw BusinessException.of(ResultCode.NO_LOGIN);
	}

}
