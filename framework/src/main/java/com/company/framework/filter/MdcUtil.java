package com.company.framework.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.google.common.collect.Maps;

public class MdcUtil {

	public final static String UNIQUE_KEY = "trace-id";

	public static void put() {
		MDC.put(UNIQUE_KEY, UUID.randomUUID().toString().replace("-", ""));
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