package com.company.common.util;

import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
public class HostUtil {

	/**
	 * 本机标识
	 * 
	 * @return
	 */
	public static String identity() {
		try {
			InetAddress s = InetAddress.getLocalHost();
			if (StringUtils.isNoneBlank(s.getHostName())) {
				return s.getHostName();
			}
			return s.getHostAddress();
		} catch (UnknownHostException e) {
			log.error("UnknownHostException", e);
		}
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(8);
		log.warn("randomAlphanumeric:{}", randomAlphanumeric);
		return randomAlphanumeric;
	}
	
	/**
	 * 本机IP
	 * 
	 * @return
	 */
	public static String ip() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return "127.0.0.1";
	}

	/**
	 * 获取当前进程 PID
	 *
	 * @return
	 */
	public static long pid() {
		return SystemUtil.getCurrentPID();
	}

	public static void main(String[] args) {
		System.out.println(HostUtil.identity());
		System.out.println(RandomStringUtils.randomAlphanumeric(8));
		System.out.println(RandomStringUtils.randomAlphanumeric(22));
		System.out.println(HostUtil.ip());
		System.out.println(HostUtil.pid());
		System.out.println(SystemUtil.getHostInfo().getName());
	}
}