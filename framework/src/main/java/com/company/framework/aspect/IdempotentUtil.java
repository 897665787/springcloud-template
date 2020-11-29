package com.company.framework.aspect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.redis.RedisHolder;
import com.google.common.collect.Maps;

public class IdempotentUtil {
	
	private static final String HEADER_IDEMPOTENT_ID = "idempotent-id";
	private static final String HEADER_IDEMPOTENT_EXPIRE_MILLIS = "idempotent-expire-millis";
	
	private static final String REDIS_HEAD_PREFIX = "idempotent:head:%s";
	private static final String REDIS_DATA_PREFIX = "idempotent:data:%s";
	private static final String REDIS_LOCK_PREFIX = "idempotent:lock:%s";
	
	private static ThreadLocal<String> tl = new ThreadLocal<>();

	/** 调用方 **/
	public static void create() {
		String idempotentId = UUID.randomUUID().toString().replace("-", "");
		tl.set(idempotentId);
	}

	public static String get() {
		return tl.get();
	}
	
	public static void set(String idempotentId) {
		tl.set(idempotentId);
	}
	
	public static void remove() {
		tl.remove();
	}
	
	public static Map<String, Collection<String>> headers() {
		String idempotentId = tl.get();
		if (idempotentId == null) {
			return Collections.emptyMap();
		}
		HashMap<String, Collection<String>> headers = Maps.newHashMap();
		headers.put(HEADER_IDEMPOTENT_ID, Arrays.asList(idempotentId));
		int expireMillis = expireMillis();
		headers.put(HEADER_IDEMPOTENT_EXPIRE_MILLIS, Arrays.asList(String.valueOf(expireMillis)));
		RedisHolder redisHolder = SpringContextUtil.getBean(RedisHolder.class);
		redisHolder.set(IdempotentUtil.head(idempotentId), StringUtils.EMPTY, expireMillis, TimeUnit.MILLISECONDS);
		return headers;
	}
	
	/** 被调用方 **/
	public static String idempotentId() {
		return HttpContextUtil.head(HEADER_IDEMPOTENT_ID);
	}

	public static Integer idempotentExpireMillis() {
		String idempotentExpireMillis = HttpContextUtil.head(HEADER_IDEMPOTENT_EXPIRE_MILLIS);
		return Integer.valueOf(idempotentExpireMillis);
	}

	public static String head(String idempotentId) {
		return String.format(REDIS_HEAD_PREFIX, idempotentId);
	}
	
	public static String data() {
		return String.format(REDIS_DATA_PREFIX, idempotentId());
	}

	public static String lock() {
		return String.format(REDIS_LOCK_PREFIX, idempotentId());
	}
	
	/**
	 * 重试次数
	 * 
	 * @return
	 */
	public static int retries() {
		int maxAutoRetries = SpringContextUtil.getIntegerProperty("ribbon.MaxAutoRetries", 0);
		int maxAutoRetriesNextServer = SpringContextUtil.getIntegerProperty("ribbon.MaxAutoRetriesNextServer", 1);
		int retries = 1 + maxAutoRetries + maxAutoRetriesNextServer + (maxAutoRetries * maxAutoRetriesNextServer);
		return retries;
	}
	
	/**
	 * 超时毫秒数
	 * 
	 * @return
	 */
	public static int expireMillis() {
		int readTimeout = SpringContextUtil.getIntegerProperty("ribbon.ReadTimeout", 1000);
		int connectTimeout = SpringContextUtil.getIntegerProperty("ribbon.ConnectTimeout", 1000);
		int expireMillis = retries() * (readTimeout + connectTimeout);
		return expireMillis;
	}
}