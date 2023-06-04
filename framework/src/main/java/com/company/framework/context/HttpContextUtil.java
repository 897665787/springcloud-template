package com.company.framework.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.company.common.constant.HeaderConstants;
import com.company.framework.util.IpUtil;
import com.google.common.collect.Maps;

public class HttpContextUtil {
	// 公共请求头（与用户无关）
	public static final String HEADER_PLATFORM = "x-platform";// 平台
	public static final String HEADER_OPERATOR = "x-operator";// 操作系统
	public static final String HEADER_VERSION = "x-version";// 版本号
	public static final String HEADER_DEVICEID = "x-deviceid";// 设备ID
	public static final String HEADER_SOURCE = "x-source";// 请求来源
	public static final String HEADER_REQUESTIP = "x-requestip";// 请求IP

	// 用户请求头（注：为了防止直接在header设置用户ID，绕过认证，要取最后1个值）
	public static final String HEADER_CURRENT_USER_ID = HeaderConstants.HEADER_CURRENT_USER_ID;// 当前登录用户id

	private HttpContextUtil() {
	}

	public static ServletRequestAttributes attributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * 请注意，请求响应之后再无法获取HttpServletRequest相关数据
	 */
	public static HttpServletRequest request() {
		return Optional.ofNullable(attributes()).map(ServletRequestAttributes::getRequest).orElse(null);
	}

	public static HttpServletResponse response() {
		return Optional.ofNullable(attributes()).map(ServletRequestAttributes::getResponse).orElse(null);
	}

	public static String head(String name) {
		return Optional.ofNullable(request()).map(request -> request.getHeader(name)).orElse(null);
	}
	
	public static String lastHead(String name) {
		HttpServletRequest request = request();
		if (request == null) {
			return null;
		}
		Enumeration<String> headerEnum = request.getHeaders(name);
		String lastHeader = null;
		while (headerEnum.hasMoreElements()) {
			lastHeader = headerEnum.nextElement();
		}
		return lastHeader;
	}

	public static String parameter(String name, String defaultValue) {
		return Optional.ofNullable(request()).map(request -> request.getParameter(name)).orElse(defaultValue);
	}

	public static int parameterInt(String name, int defaultValue) {
		return Optional.ofNullable(parameter(name, null)).map(Integer::valueOf).orElse(defaultValue);
	}

	public static String currentUserId() {
		return lastHead(HEADER_CURRENT_USER_ID);
	}

	public static String platform() {
		return head(HEADER_PLATFORM);
	}

	public static String operator() {
		return head(HEADER_OPERATOR);
	}

	public static String version() {
		return head(HEADER_VERSION);
	}

	public static String deviceid() {
		return head(HEADER_DEVICEID);
	}

	public static String source() {
		return head(HEADER_SOURCE);
	}
	
	public static String requestip() {
		return head(HEADER_REQUESTIP);
	}
	
	public static Map<String, String> httpContextHeader() {
		Map<String, String> headers = Maps.newHashMap();
		headers.put(HEADER_CURRENT_USER_ID, currentUserId());
		headers.put(HEADER_PLATFORM, platform());
		headers.put(HEADER_OPERATOR, operator());
		headers.put(HEADER_VERSION, version());
		headers.put(HEADER_DEVICEID, deviceid());
		headers.put(HEADER_SOURCE, source());
		headers.put(HEADER_REQUESTIP, requestip());
		return headers;
	}
	
	public static Map<String, String> httpContextHeaderThisRequest(HttpServletRequest request) {
		Map<String, String> headers = Maps.newHashMap();
		
		Enumeration<String> headerEnum = request.getHeaders(HttpContextUtil.HEADER_CURRENT_USER_ID);
		String lastCurrentUserId = null;
		while (headerEnum.hasMoreElements()) {
			lastCurrentUserId = headerEnum.nextElement();
		}
		headers.put(HEADER_CURRENT_USER_ID, lastCurrentUserId);
		
		headers.put(HEADER_PLATFORM, request.getHeader(HEADER_PLATFORM));
		headers.put(HEADER_OPERATOR, request.getHeader(HEADER_OPERATOR));
		headers.put(HEADER_VERSION, request.getHeader(HEADER_VERSION));
		headers.put(HEADER_DEVICEID, request.getHeader(HEADER_DEVICEID));
		headers.put(HEADER_SOURCE, request.getHeader(HEADER_SOURCE));
		headers.put(HEADER_REQUESTIP, request.getHeader(HEADER_REQUESTIP));
		return headers;
	}
	
	public static Map<String, Collection<String>> httpContextHeaders() {
		Set<Entry<String, String>> entrySet = httpContextHeader().entrySet();
		
		Map<String, Collection<String>> headers = Maps.newHashMap();
		for (Entry<String, String> entry : entrySet) {
			headers.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}
		return headers;
	}
	
	public static String getRequestIp() {
		return IpUtil.getRequestIp(request());
	}
}
