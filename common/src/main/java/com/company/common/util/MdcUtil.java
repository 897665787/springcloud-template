package com.company.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.google.common.collect.Maps;

public class MdcUtil {

	public final static String UNIQUE_KEY = "trace-id";

	/**
	 * 1.Random是线程安全的<br/>
	 * 2.高并发情况下，单实例的性能不如每个线程持有一个实例<br/>
	 * 3.经粗略测试，并发数少于200情况下性能是单实例优，按需来说项目目前并发量在200内<br/>
	 */
	private static Random random = new Random();
	
	public static void put() {
		String uuid = "" + System.currentTimeMillis() + (1000 + random.nextInt(9000));
		MDC.put(UNIQUE_KEY, uuid);
	}
	
	public static void put(String traceId) {
		if (StringUtils.isNotEmpty(traceId)) {
			MDC.put(UNIQUE_KEY, traceId);
			return;
		}
		put();
	}

	public static String get() {
		return MDC.get(UNIQUE_KEY);
	}
	
	public static void remove() {
		MDC.remove(UNIQUE_KEY);
	}
	
	public static Map<String, Collection<String>> headers() {
		HashMap<String, Collection<String>> headers = Maps.newHashMap();
		headers.put(UNIQUE_KEY, Arrays.asList(get()));
		return headers;
	}
}