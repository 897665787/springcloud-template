package com.company.framework.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.company.framework.cache.exception.ValueRetrievalException;

/**
 * 组合缓存（先查redis，redis不可用的情况下使用本地缓存）
 */
public class CombinationCache implements ICache {

	private ICache redisCache;
	private ICache guavaCache;

	public CombinationCache(ICache redisCache, ICache guavaCache) {
		this.redisCache = redisCache;
		this.guavaCache = guavaCache;
	}

	@Override
	public void set(String key, String value) {
		try {
			redisCache.set(key, value);
		} catch (ValueRetrievalException e) {
			// 如果是redis导致的异常，就用本地缓存
			guavaCache.set(key, value);
		}
	}

	@Override
	public void set(String key, String value, long timeout, TimeUnit unit) {
		try {
			redisCache.set(key, value, timeout, unit);
		} catch (ValueRetrievalException e) {
			// 如果是redis导致的异常，就用本地缓存
			guavaCache.set(key, value, timeout, unit);
		}
	}
	
	@Override
	public String get(String key) {
		try {
			return redisCache.get(key);
		} catch (ValueRetrievalException e) {
			// 如果是redis导致的异常，就用本地缓存
			return guavaCache.get(key);
		}
	}

	@Override
	public String get(String key, Callable<String> valueLoader) {
		String value = null;
		try {
			value = redisCache.get(key, valueLoader);
		} catch (ValueRetrievalException e) {
			try {
				// 如果是redis导致的异常，就用本地缓存
				value = guavaCache.get(key, valueLoader);
			} catch (ValueRetrievalException e2) {
				// 如果是redis和本地缓存都异常，就直接执行valueLoader获取数据
				if (valueLoader != null) {
					try {
						value = valueLoader.call();
					} catch (Exception e1) {
						throw new RuntimeException(e1);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return value;
	}

	@Override
	public boolean del(String key) {
		try {
			return redisCache.del(key);
		} catch (ValueRetrievalException e) {
			// 如果是redis导致的异常，就用本地缓存
			return guavaCache.del(key);
		}
	}

	@Override
	public long increment(String key, long delta) {
		try {
			return redisCache.increment(key, delta);
		} catch (ValueRetrievalException e) {
			// 如果是redis导致的异常，就用本地缓存
			return guavaCache.increment(key, delta);
		}
	}

}