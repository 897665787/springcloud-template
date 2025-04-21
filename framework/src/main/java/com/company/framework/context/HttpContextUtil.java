package com.company.framework.context;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.company.common.constant.HeaderConstants;
import com.company.framework.util.IpUtil;
import com.google.common.collect.Maps;

public class HttpContextUtil {
	// 公共请求头（与用户无关）
	public static final String HEADER_PLATFORM = HeaderConstants.HEADER_PLATFORM;// 平台：app(APP)、mini(微信小程序)、h5(H5页面)、alimini(支付宝小程序)
	// 、alipaymini(支付宝小程序内H5)
	public static final String HEADER_OPERATOR = HeaderConstants.HEADER_OPERATOR;// 操作系统：ios(iOS)、mac(iOS)、android(Android)、win
	// (Windows,如果获取不到可以不要)、devtools(小程序开发工具)
	public static final String HEADER_VERSION = HeaderConstants.HEADER_VERSION;// 版本号：4.1.0
	public static final String HEADER_DEVICEID = HeaderConstants.HEADER_DEVICEID;// 设备ID：82b6fe22b2063733af477a8df7358238
	public static final String HEADER_CHANNEL = HeaderConstants.HEADER_CHANNEL;// 渠道：wx(微信小程序)、ali(支付宝小程序)、dev(开发包)、sit(测试包)、uat
	// (验收包)、ios(苹果应用商店)、xiaomi(小米应用商店)、huawei(华为应用商店)等
	public static final String HEADER_REQUESTIP = HeaderConstants.HEADER_REQUESTIP;// 请求IP（最外层的请求）
	public static final String HEADER_SOURCE = HeaderConstants.HEADER_SOURCE;// 用户来源：pyqgg(朋友圈广告)、dygg(抖音广告)、dt_15333223333(张三地推)
	// 、fx_oQvXm5qqQIiEPewZTtOKugyuEGWA(用户分享)、fx_rebate_oQvXm5qqQIiEPewZTtOKugyuEGWA(用户返现分享)

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

	public static Integer currentUserIdInt() {
		return Optional.ofNullable(currentUserId()).map(Integer::valueOf).orElse(null);
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

	public static String channel() {
		return head(HEADER_CHANNEL);
	}
	
	public static String requestip() {
		String requestip = head(HEADER_REQUESTIP);
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
		return head(HEADER_SOURCE);
	}
	
	public static Map<String, String> httpContextHeader() {
		Map<String, String> headers = Maps.newHashMap();
		headers.put(HEADER_CURRENT_USER_ID, currentUserId());
		headers.put(HEADER_PLATFORM, platform());
		headers.put(HEADER_OPERATOR, operator());
		headers.put(HEADER_VERSION, version());
		headers.put(HEADER_DEVICEID, deviceid());
		headers.put(HEADER_CHANNEL, channel());
		headers.put(HEADER_REQUESTIP, requestip());
		headers.put(HEADER_SOURCE, source());
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
		headers.put(HEADER_CHANNEL, request.getHeader(HEADER_CHANNEL));
		headers.put(HEADER_REQUESTIP, request.getHeader(HEADER_REQUESTIP));
		headers.put(HEADER_SOURCE, request.getHeader(HEADER_SOURCE));
		return headers;
	}
	
	public static Map<String, List<String>> httpContextHeaders() {
		Set<Entry<String, String>> entrySet = httpContextHeader().entrySet();
		
		Map<String, List<String>> headers = Maps.newHashMap();
		for (Entry<String, String> entry : entrySet) {
			headers.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}
		return headers;
	}
	
	public static String getRequestIp() {
		return IpUtil.getRequestIp(request());
	}
}
