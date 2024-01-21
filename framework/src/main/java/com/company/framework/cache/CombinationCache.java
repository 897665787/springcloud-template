package com.company.framework.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

import com.company.framework.cache.exception.ValueRetrievalException;

import lombok.extern.slf4j.Slf4j;

/**
 * 组合缓存（先查redis，redis不可用的情况下使用本地缓存）
 */
@Slf4j
public class CombinationCache implements ICache {

	int openingThreshold = 10;// 单位时间内请求超过openingThreshold就打开断路器
	long openingInterval = 10;
	TimeUnit openingUnit = TimeUnit.SECONDS;

	int closingThreshold = 5;// 单位时间内请求小于closingThreshold就关闭断路器
	long closingInterval = 10;
	TimeUnit closingUnit = TimeUnit.SECONDS;

	// 断路器
	EventCountCircuitBreaker breaker = new EventCountCircuitBreaker(openingThreshold, openingInterval, openingUnit,
			closingThreshold, closingInterval, closingUnit);
	{
		breaker.addChangeListener(event -> {
			log.info("event:{}", event);
		});
	}

	private ICache redisCache;
	private ICache guavaCache;
	
	public CombinationCache(ICache redisCache, ICache guavaCache) {
		this.redisCache = redisCache;
		this.guavaCache = guavaCache;
	}

	@Override
	public void set(String key, String value) {
		breakerNoReturn(() -> redisCache.set(key, value), () -> guavaCache.set(key, value));
	}

	@Override
	public void set(String key, String value, long timeout, TimeUnit unit) {
		breakerNoReturn(() -> redisCache.set(key, value, timeout, unit),
				() -> guavaCache.set(key, value, timeout, unit));
	}

	@Override
	public String get(String key) {
		return breakerReturn(() -> redisCache.get(key), () -> guavaCache.get(key));
	}

	@Override
	public String get(String key, Callable<String> valueLoader) {
		return breakerReturn(() -> redisCache.get(key, valueLoader), () -> guavaCache.get(key, valueLoader));
	}

	@Override
	public boolean del(String key) {
		return breakerReturn(() -> redisCache.del(key), () -> guavaCache.del(key));
	}

	@Override
	public long increment(String key, long delta) {
		return breakerReturn(() -> redisCache.increment(key, delta), () -> guavaCache.increment(key, delta));
	}

	@Override
	public long increment(String key, long delta, long timeout, TimeUnit unit) {
		return breakerReturn(() -> redisCache.increment(key, delta, timeout, unit),
				() -> guavaCache.increment(key, delta, timeout, unit));
	}

	private <T> T breakerReturn(Supplier<T> redisSupplier, Supplier<T> guavaSupplier) {
		if (!breaker.checkState()) {
			return guavaSupplier.get();
		}
		try {
			return redisSupplier.get();
		} catch (ValueRetrievalException e) {
			breaker.incrementAndCheckState();
			// 如果是redis导致的异常，就用本地缓存
			return guavaSupplier.get();
		}
	}

	private void breakerNoReturn(Runnable redisRunnable, Runnable guavaRunnable) {
		if (!breaker.checkState()) {
			guavaRunnable.run();
			return;
		}
		try {
			redisRunnable.run();
		} catch (ValueRetrievalException e) {
			breaker.incrementAndCheckState();
			// 如果是redis导致的异常，就用本地缓存
			guavaRunnable.run();
		}
	}
}