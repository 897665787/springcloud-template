package com.company.adminapi.controller;

/**
 * 缓存监控
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.RequirePermissions;

/**
 * 缓存监控（基于 Redis）
 */
@RestController
@RequestMapping("/monitor")
public class CacheController {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * Redis 基本信息
	 */
	@RequirePermissions("monitor:cache:list")
	@GetMapping("/cache")
	public Map<String, Object> cache() {
		Map<String, Object> result = new HashMap<>();
		Properties info = redisInfo();
		result.put("info", toMap(info));
		result.put("dbSize", dbSize());
		result.put("commandStats", new ArrayList<>());
		return result;
	}

	/**
	 * 缓存名称列表（Redis 的 db 列表）
	 */
	@RequirePermissions("monitor:cache:list")
	@GetMapping("/cache/getNames")
	public List<Map<String, Object>> getNames() {
		List<Map<String, Object>> names = new ArrayList<>();
		Map<String, Object> name = new HashMap<>();
		name.put("cacheName", "redis:db0");
		name.put("remark", "Redis 默认库");
		names.add(name);
		return names;
	}

	/**
	 * 缓存键名列表
	 */
	@RequirePermissions("monitor:cache:list")
	@GetMapping("/cache/getKeys/{cacheName}")
	public List<String> getKeys(@PathVariable String cacheName) {
		Set<Object> keys = redisTemplate.keys("*");
		List<String> result = new ArrayList<>();
		if (keys != null) {
			keys.forEach(key -> result.add(String.valueOf(key)));
		}
		return result;
	}

	/**
	 * 缓存内容
	 */
	@RequirePermissions("monitor:cache:list")
	@GetMapping("/cache/getValue/{cacheName}/{cacheKey}")
	public Map<String, Object> getValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
		Object value = redisTemplate.opsForValue().get(cacheKey);
		Map<String, Object> result = new HashMap<>();
		result.put("cacheName", cacheName);
		result.put("cacheKey", cacheKey);
		result.put("cacheValue", value == null ? "" : String.valueOf(value));
		return result;
	}

	/**
	 * 清空缓存名称下的所有键
	 */
	@RequirePermissions("monitor:cache:remove")
	@DeleteMapping("/cache/clearCacheName/{cacheName}")
	public Boolean clearCacheName(@PathVariable String cacheName) {
		Set<Object> keys = redisTemplate.keys("*");
		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
		return true;
	}

	/**
	 * 清空指定键
	 */
	@RequirePermissions("monitor:cache:remove")
	@DeleteMapping("/cache/clearCacheKey/{cacheKey}")
	public Boolean clearCacheKey(@PathVariable String cacheKey) {
		redisTemplate.delete(cacheKey);
		return true;
	}

	/**
	 * 清空全部缓存
	 */
	@RequirePermissions("monitor:cache:remove")
	@DeleteMapping("/cache/clearCacheAll")
	public Boolean clearCacheAll() {
		RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
		if (factory == null) {
			return false;
		}
		try (RedisConnection connection = factory.getConnection()) {
			connection.serverCommands().flushDb();
		}
		return true;
	}

	/**
	 * 读取 redis info
	 */
	private Properties redisInfo() {
		RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
		if (factory == null) {
			return new Properties();
		}
		try (RedisConnection connection = factory.getConnection()) {
			Properties properties = connection.serverCommands().info();
			return properties == null ? new Properties() : properties;
		}
	}

	/**
	 * 读取 db 键数量
	 */
	private Long dbSize() {
		RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
		if (factory == null) {
			return 0L;
		}
		try (RedisConnection connection = factory.getConnection()) {
			return connection.serverCommands().dbSize();
		}
	}

	/**
	 * Properties 转 Map
	 */
	private Map<String, Object> toMap(Properties properties) {
		Map<String, Object> map = new HashMap<>();
		properties.stringPropertyNames().forEach(name -> map.put(name, properties.getProperty(name)));
		return map;
	}
}
