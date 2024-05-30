package com.company.adminapi.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.adminapi.annotation.RequirePermissions;
import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "permission", havingValue = "true", matchIfMissing = true)
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		Method method = handlerMethod.getMethod();
		
		RequirePermissions methodAnnotation = AnnotationUtils.findAnnotation(method, RequirePermissions.class);
		RequirePermissions classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RequirePermissions.class);

		if (methodAnnotation == null && classAnnotation == null) {
			// 方法和类上都没有打注解RequirePermissions
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
		if (StringUtils.isBlank(userId)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			Result<?> fail = Result.fail(ResultCode.NO_LOGIN);
			writer.write(JsonUtil.toJsonString(fail));
			log.warn("登录过期:{}.{}", method.getDeclaringClass().getName(), method.getName());

			return false;
		}

		// 判断是否有访问权限？
		RequirePermissions requirePermissions = method.getAnnotation(RequirePermissions.class);
		boolean hasPermissions = hasPermissions(Integer.valueOf(userId), requirePermissions.value());
		if (hasPermissions) {
			return true;
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		Result<?> fail = Result.fail(ResultCode.NO_PERMISSION);
		writer.write(JsonUtil.toJsonString(fail));
		log.warn("无访问权限:{}.{}", method.getDeclaringClass().getName(), method.getName());
		
		return false;
	}

	/** 所有权限标识 */
	private static final String ALL_PERMISSION = "*:*:*";

	/**
	 * 验证用户是否含有指定权限，必须全部拥有
	 *
	 * @param userId
	 * @param permissions
	 *            权限列表
	 */
	private boolean hasPermissions(Integer userId, String... permissions) {
		// Set<String> permissionList = getPermiList();
		Set<String> permissionList = Sets.newHashSet("system:config:add", "system:config:update",
				"system:config:delete");// TODO load db
		for (String permission : permissions) {
			if (!hasPermi(permissionList, permission)) {
				// throw new BusinessException(permission);
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否包含权限
	 * 
	 * @param authorities
	 *            权限列表
	 * @param permission
	 *            权限字符串
	 * @return 用户是否具备某权限
	 */
	private boolean hasPermi(Collection<String> authorities, String permission) {
		return authorities.stream().filter(StringUtils::isNotBlank)
				.anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
	}
}
