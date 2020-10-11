package com.company.framework.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;

public class HttpContextUtil {
	// 当前登录用户id
	public static final String HEADER_CURRENT_USER_ID = "$current-user-id$";

	private HttpContextUtil() {
	}

	public static ServletRequestAttributes attributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	public static HttpServletRequest request() {
		return Optional.ofNullable(attributes()).map(ServletRequestAttributes::getRequest).orElse(null);
	}

	public static HttpServletResponse response() {
		return Optional.ofNullable(attributes()).map(ServletRequestAttributes::getResponse).orElse(null);
	}

	public static String currentUserId() {
		return Optional.ofNullable(request()).map(request -> request.getHeader(HEADER_CURRENT_USER_ID)).orElse(null);
	}

	public static Map<String, Collection<String>> currentUserHeaders() {
		Map<String, Collection<String>> headers = Maps.newHashMap();
		headers.put(HEADER_CURRENT_USER_ID, Arrays.asList(currentUserId()));
		return headers;
	}
}
