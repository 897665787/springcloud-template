package com.company.framework.cache;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.company.common.util.JsonUtil;

public interface ICache {

	void set(String key, String value);

	void set(String key, String value, long timeout, TimeUnit unit);

	default void set(String key, Object value) {
		set(key, JsonUtil.toJsonString(value));
	}

	default void set(String key, Object value, long timeout, TimeUnit unit) {
		set(key, JsonUtil.toJsonString(value), timeout, unit);
	}

	String get(String key);

	String get(String key, Callable<String> valueLoader);

	boolean del(String key);

	default <T> T get(String key, Class<T> clazz) {
		String value = get(key);
		return JsonUtil.toEntity(value, clazz);
	}

	default <T> T get(String key, Callable<String> valueLoader, Class<T> clazz) {
		String value = get(key, valueLoader);
		return JsonUtil.toEntity(value, clazz);
	}

	default <T> List<T> getList(String key, Callable<String> valueLoader, Class<T> clazz) {
		String value = get(key, valueLoader);
		return JsonUtil.toList(value, clazz);
	}

	long increment(String key, long delta);
	
	long increment(String key, long delta, long timeout, TimeUnit unit);
	
}