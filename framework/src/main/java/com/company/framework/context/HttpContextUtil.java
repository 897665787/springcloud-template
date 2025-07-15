package com.company.framework.context;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.company.framework.constant.HeaderConstants;
import com.company.framework.util.IpUtil;
import com.google.common.collect.Maps;

public class HttpContextUtil {

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

	public static Integer currentUserIdInt() {
		return Optional.ofNullable(currentUserId()).map(Integer::valueOf).orElse(null);
	}

	public static String currentUserId() {
		return lastHead(HeaderConstants.HEADER_CURRENT_USER_ID);
	}

	public static String platform() {
		return head(HeaderConstants.HEADER_PLATFORM);
	}

	public static String operator() {
		return head(HeaderConstants.HEADER_OPERATOR);
	}

	public static String version() {
		return head(HeaderConstants.HEADER_VERSION);
	}

	public static String deviceid() {
		return head(HeaderConstants.HEADER_DEVICEID);
	}

	public static String channel() {
		return head(HeaderConstants.HEADER_CHANNEL);
	}

	public static String requestip() {
		String requestip = head(HeaderConstants.HEADER_REQUESTIP);
		if (StringUtils.isNotBlank(requestip)) {
			return requestip;
		}
		HttpServletRequest request = request();
		if (request == null) {
			return "127.0.0.1";
		} else {
			return IpUtil.getRequestIp(request);// 后续需要完善成链式传输
		}
	}


	public static String source() {
		return head(HeaderConstants.HEADER_SOURCE);
	}

	public static Map<String, String> httpContextHeader() {
		Map<String, String> headers = Maps.newHashMap();
		headers.put(HeaderConstants.HEADER_CURRENT_USER_ID, currentUserId());
		headers.put(HeaderConstants.HEADER_PLATFORM, platform());
		headers.put(HeaderConstants.HEADER_OPERATOR, operator());
		headers.put(HeaderConstants.HEADER_VERSION, version());
		headers.put(HeaderConstants.HEADER_DEVICEID, deviceid());
		headers.put(HeaderConstants.HEADER_CHANNEL, channel());
		headers.put(HeaderConstants.HEADER_REQUESTIP, requestip());
		headers.put(HeaderConstants.HEADER_SOURCE, source());
		return headers;
	}

	public static Map<String, String> httpContextHeaderThisRequest(HttpServletRequest request) {
		Map<String, String> headers = Maps.newHashMap();

		Enumeration<String> headerEnum = request.getHeaders(HeaderConstants.HEADER_CURRENT_USER_ID);
		String lastCurrentUserId = null;
		while (headerEnum.hasMoreElements()) {
			lastCurrentUserId = headerEnum.nextElement();
		}
		headers.put(HeaderConstants.HEADER_CURRENT_USER_ID, lastCurrentUserId);

		headers.put(HeaderConstants.HEADER_PLATFORM, request.getHeader(HeaderConstants.HEADER_PLATFORM));
		headers.put(HeaderConstants.HEADER_OPERATOR, request.getHeader(HeaderConstants.HEADER_OPERATOR));
		headers.put(HeaderConstants.HEADER_VERSION, request.getHeader(HeaderConstants.HEADER_VERSION));
		headers.put(HeaderConstants.HEADER_DEVICEID, request.getHeader(HeaderConstants.HEADER_DEVICEID));
		headers.put(HeaderConstants.HEADER_CHANNEL, request.getHeader(HeaderConstants.HEADER_CHANNEL));
		headers.put(HeaderConstants.HEADER_REQUESTIP, request.getHeader(HeaderConstants.HEADER_REQUESTIP));
		headers.put(HeaderConstants.HEADER_SOURCE, request.getHeader(HeaderConstants.HEADER_SOURCE));
		return headers;
	}

	public static Map<String, List<String>> httpContextHeaders() {
		Set<Entry<String, String>> entrySet = httpContextHeader().entrySet();

		Map<String, List<String>> headers = Maps.newHashMap();
		for (Entry<String, String> entry : entrySet) {
			headers.put(entry.getKey(), Collections.singletonList(entry.getValue()));
		}
		return headers;
	}

	public static String getRequestIp() {
		return IpUtil.getRequestIp(request());
	}
}
