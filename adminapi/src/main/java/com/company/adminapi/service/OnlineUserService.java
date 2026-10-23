package com.company.adminapi.service;

/**
 * 在线用户服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.company.adminapi.dto.OnlineSession;
import com.company.framework.util.JsonUtil;

/**
 * 在线用户服务（基于 Redis 维护 token 会话）
 * <p>
 * 项目使用无状态 JWT，token 本身不可撤销，因此额外维护一份会话表用于「在线用户」查询，
 * 同时维护黑名单用于「强退」。
 */
@Service
public class OnlineUserService {

	// 在线会话 key 前缀
	private static final String ONLINE_KEY_PREFIX = "admin:online:";

	// 强制下线黑名单 key 前缀
	private static final String BLACKLIST_KEY_PREFIX = "admin:blacklist:";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 记录登录会话
	 */
	public void save(OnlineSession session) {
		String key = ONLINE_KEY_PREFIX + session.getTokenId();
		stringRedisTemplate.opsForValue().set(key, JsonUtil.toJsonString(session));
	}

	/**
	 * 删除登录会话
	 */
	public void remove(String token) {
		stringRedisTemplate.delete(ONLINE_KEY_PREFIX + token);
	}

	/**
	 * 加入黑名单（强退）
	 */
	public void blacklist(String token, Long expireSeconds) {
		stringRedisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + token, "1",
				Duration.ofSeconds(expireSeconds == null ? 86400L : expireSeconds));
	}

	/**
	 * 是否在黑名单中
	 */
	public boolean isBlacklisted(String token) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(BLACKLIST_KEY_PREFIX + token));
	}

	/**
	 * 查询在线会话列表
	 */
	public List<OnlineSession> list() {
		Set<String> keys = stringRedisTemplate.keys(ONLINE_KEY_PREFIX + "*");
		List<OnlineSession> sessions = new ArrayList<>();
		if (keys == null || keys.isEmpty()) {
			return sessions;
		}
		keys.forEach(key -> {
			String json = stringRedisTemplate.opsForValue().get(key);
			if (json != null) {
				OnlineSession session = JsonUtil.toEntity(json, OnlineSession.class);
				if (session != null) {
					sessions.add(session);
				}
			}
		});
		sessions.sort((a, b) -> {
			LocalDateTime t1 = a.getLoginTime();
			LocalDateTime t2 = b.getLoginTime();
			if (t1 == null || t2 == null) {
				return 0;
			}
			return t2.compareTo(t1);
		});
		return sessions;
	}
}
