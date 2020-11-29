package com.company.framework.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.company.common.util.JsonUtil;

public class RedisHolder {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public void set(String key, String value) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(key, value);
	}
	
	public void set(String key, String value, long timeout, TimeUnit unit) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(key, value, timeout, unit);
	}
	
	public String get(String key) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		return opsForValue.get(key);
	}
	
	public void set(String key, Object value) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(key, JsonUtil.toJsonString(value));
	}
	
	public void set(String key, Object value, long timeout, TimeUnit unit) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(key, JsonUtil.toJsonString(value), timeout, unit);
	}

	public <T> T get(String key, Class<T> clazz) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		return JsonUtil.toEntity(opsForValue.get(key), clazz);
	}
	
	public <T> List<T> getList(String key, Class<T> clazz) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		return JsonUtil.toList(opsForValue.get(key), clazz);
	}
	
	public Long increment(String key, long delta) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		return opsForValue.increment(key, delta);
	}
	
	public Boolean del(String key) {
		return stringRedisTemplate.delete(key);
	}
	
}