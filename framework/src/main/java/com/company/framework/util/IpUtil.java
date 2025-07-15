package com.company.framework.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {

	public static String getHostIp() {
		return HostUtil.ip();
		/*
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("error : ", e);
		}
		return ip;
		*/
	}

	public static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = ip.split(",");
			for (int i = 0; i < ips.length; i++) {
				if (!"unknown".equalsIgnoreCase(ips[i])) {
					ip = ips[i];
					break;
				}
			}
		}
		if (ipIsNull(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ipIsNull(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipIsNull(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipIsNull(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipIsNull(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ipIsNull(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					log.error("error : ", e);
				}
			}
		}
		return ip;
	}

	private static boolean ipIsNull(String ip) {
		return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip) || ip.length() > 15;
	}
}
