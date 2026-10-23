package com.company.adminapi.controller;

/**
 * 服务器监控
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.RequirePermissions;

import java.lang.management.OperatingSystemMXBean;

/**
 * 服务器监控
 */
@RestController
@RequestMapping("/monitor")
public class ServerController {

	// 字节换算为 GB
	private static final double BYTES_OF_GB = 1024 * 1024 * 1024.0;

	// 字节换算为 MB
	private static final double BYTES_OF_MB = 1024 * 1024.0;

	// 百分比基数
	private static final double PERCENT = 100.0;

	/**
	 * 获取服务器信息（CPU、内存、JVM、系统）
	 */
	@RequirePermissions("monitor:server:list")
	@GetMapping("/server")
	public Map<String, Object> server() {
		Map<String, Object> result = new HashMap<>();
		result.put("cpu", cpuInfo());
		result.put("mem", memInfo());
		result.put("jvm", jvmInfo());
		result.put("sys", sysInfo());
		return result;
	}

	/**
	 * CPU 信息
	 */
	private Map<String, Object> cpuInfo() {
		Map<String, Object> cpu = new HashMap<>();
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		cpu.put("cpuNum", os.getAvailableProcessors());
		cpu.put("used", percent(loadAverage()));
		cpu.put("sys", percent(loadAverage()));
		cpu.put("free", percent(1 - loadAverage()));
		return cpu;
	}

	/**
	 * 物理内存信息
	 */
	private Map<String, Object> memInfo() {
		Map<String, Object> mem = new HashMap<>();
		long total = Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		if (ManagementFactory.getOperatingSystemMXBean() instanceof com.sun.management.OperatingSystemMXBean) {
			com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory
					.getOperatingSystemMXBean();
			total = os.getTotalPhysicalMemorySize();
			long free = os.getFreePhysicalMemorySize();
			long used = total - free;
			mem.put("total", round(total / BYTES_OF_GB));
			mem.put("used", round(used / BYTES_OF_GB));
			mem.put("free", round(free / BYTES_OF_GB));
			mem.put("usage", percent(total == 0 ? 0 : (double) used / total));
		}
		if (!mem.containsKey("total")) {
			mem.put("total", round(total / BYTES_OF_GB));
			mem.put("used", 0);
			mem.put("free", round(total / BYTES_OF_GB));
			mem.put("usage", 0);
		}
		return mem;
	}

	/**
	 * JVM 信息
	 */
	private Map<String, Object> jvmInfo() {
		Map<String, Object> jvm = new HashMap<>();
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
		long total = heap.getMax() > 0 ? heap.getMax() : heap.getCommitted();
		long used = heap.getUsed();
		jvm.put("total", round(total / BYTES_OF_MB));
		jvm.put("used", round(used / BYTES_OF_MB));
		jvm.put("free", round((total - used) / BYTES_OF_MB));
		jvm.put("usage", percent(total == 0 ? 0 : (double) used / total));

		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		jvm.put("name", runtimeMXBean.getVmName());
		jvm.put("version", runtimeMXBean.getVmVersion());
		jvm.put("startTime", format(runtimeMXBean.getStartTime()));
		return jvm;
	}

	/**
	 * 操作系统信息
	 */
	private Map<String, Object> sysInfo() {
		Map<String, Object> sys = new HashMap<>();
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		sys.put("osName", os.getName());
		sys.put("osArch", os.getArch());
		sys.put("computerIp", localIp());
		sys.put("computerName", localHostName());
		return sys;
	}

	/**
	 * 系统负载（0~1）
	 */
	private double loadAverage() {
		if (ManagementFactory.getOperatingSystemMXBean() instanceof com.sun.management.OperatingSystemMXBean) {
			com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory
					.getOperatingSystemMXBean();
			double load = os.getSystemCpuLoad();
			if (load >= 0) {
				return load;
			}
		}
		double loadAverage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		int processors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		if (loadAverage < 0 || processors <= 0) {
			return 0;
		}
		return Math.min(1, loadAverage / processors);
	}

	/**
	 * 本机IP
	 */
	private String localIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 本机名称
	 */
	private String localHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 保留两位小数
	 */
	private double round(double value) {
		return Math.round(value * PERCENT) / PERCENT;
	}

	/**
	 * 转为百分比（两位小数）
	 */
	private double percent(double ratio) {
		return Math.round(ratio * PERCENT * PERCENT) / PERCENT;
	}

	/**
	 * 时间戳格式化
	 */
	private String format(long millis) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()).toString();
	}
}
