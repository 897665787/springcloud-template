package com.company.adminapi.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 登录失败次数与账号锁定服务
 * <p>
 * 连续登录失败达到上限后锁定账号一段时间，登录成功后清零；
 * 管理后台「登录日志-解锁」即可清除锁定。
 */
@Service
public class LoginAttemptService {

	// 登录失败次数 key 前缀
	private static final String FAIL_COUNT_KEY_PREFIX = "admin:login:fail:";

	// 账号锁定 key 前缀
	private static final String LOCK_KEY_PREFIX = "admin:login:lock:";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Value("${sys.login.maxFailCount:5}")
	private int maxFailCount;

	@Value("${sys.login.lockMinutes:10}")
	private int lockMinutes;

	/**
	 * 账号是否被锁定
	 */
	public boolean isLocked(String account) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(LOCK_KEY_PREFIX + account));
	}

	/**
	 * 记录一次登录失败，达到上限时锁定账号
	 */
	public void recordFailure(String account) {
		String key = FAIL_COUNT_KEY_PREFIX + account;
		Long count = stringRedisTemplate.opsForValue().increment(key);
		if (count != null && count == 1L) {
			stringRedisTemplate.expire(key, Duration.ofMinutes(lockMinutes));
		}
		if (count != null && count >= maxFailCount) {
			stringRedisTemplate.opsForValue().set(LOCK_KEY_PREFIX + account, "1", Duration.ofMinutes(lockMinutes));
			stringRedisTemplate.delete(key);
		}
	}

	/**
	 * 登录成功后清除失败记录
	 */
	public void clear(String account) {
		stringRedisTemplate.delete(FAIL_COUNT_KEY_PREFIX + account);
		stringRedisTemplate.delete(LOCK_KEY_PREFIX + account);
	}

	/**
	 * 解锁账号
	 */
	public void unlock(String account) {
		this.clear(account);
	}
}
