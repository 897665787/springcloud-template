package com.company.framework.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.company.common.util.JsonUtil;

public class RedisUtils {
//	private static StringRedisTemplate stringRedisTemplate = SpringContextUtil.getBean(template().class);
	private static StringRedisTemplate stringRedisTemplate = null;

	private RedisUtils() {
	}

	public static void init(StringRedisTemplate template) {
		stringRedisTemplate = template;
	}

	private static StringRedisTemplate template() {
		if(stringRedisTemplate == null){
			throw new IllegalArgumentException("stringRedisTemplate not init");
		}
		return stringRedisTemplate;
	}
	
	public static void set(String key, String value) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		opsForValue.set(key, value);
	}

	public static void set(String key, String value, long timeout, TimeUnit unit) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		opsForValue.set(key, value, timeout, unit);
	}

	public static String get(String key) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		return opsForValue.get(key);
	}

	public static void set(String key, Object value) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		opsForValue.set(key, JsonUtil.toJsonString(value));
	}

	public static void set(String key, Object value, long timeout, TimeUnit unit) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		opsForValue.set(key, JsonUtil.toJsonString(value), timeout, unit);
	}

	public static <T> T get(String key, Class<T> clazz) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		return JsonUtil.toEntity(opsForValue.get(key), clazz);
	}

	public static <T> List<T> getList(String key, Class<T> clazz) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		return JsonUtil.toList(opsForValue.get(key), clazz);
	}

	public static Long increment(String key, long delta) {
		ValueOperations<String, String> opsForValue = template().opsForValue();
		return opsForValue.increment(key, delta);
	}

	public static Boolean del(String key) {
		return template().delete(key);
	}

}