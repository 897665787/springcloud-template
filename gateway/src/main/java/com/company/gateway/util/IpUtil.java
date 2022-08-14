package com.company.gateway.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {

	public static String getRequestIp(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String ip = headers.getFirst("x-forwarded-for");
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
			ip = headers.getFirst("Proxy-Client-IP");
		}
		if (ipIsNull(ip)) {
			ip = headers.getFirst("WL-Proxy-Client-IP");
		}
		if (ipIsNull(ip)) {
			ip = headers.getFirst("HTTP_CLIENT_IP");
		}
		if (ipIsNull(ip)) {
			ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
		}
		if (ipIsNull(ip)) {
			ip = headers.getFirst("X-Real-IP");
		}
		if (ipIsNull(ip)) {
			ip = request.getRemoteAddress().getAddress().getHostAddress();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					log.error("error : ", e);
				}
				ip = inet.getHostAddress();
			}
		}
		return ip;
	}

	private static boolean ipIsNull(String ip) {
		return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip) || ip.length() > 15;
	}
}
