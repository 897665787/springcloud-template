package com.company.framework.lock.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.company.framework.lock.LockClient;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JvmLockClient implements LockClient {
	private Cache<String, ReentrantLock> locks = CacheBuilder.newBuilder()//
			.maximumSize(10000)//
			.expireAfterAccess(10, TimeUnit.SECONDS)//
			.removalListener(listener -> {
				log.info("key:{},value:{},cause:{}", listener.getKey(), listener.getValue(), listener.getCause());
			}).build();

	@Override
	public <V> V doInLock(String name, Supplier<V> supplier) {
		ReentrantLock lock = getLock(name);
		try {
			lock.lock();
			return supplier.get();
		} catch (Exception e) {
			throw e;
		} finally {
			lock.unlock();
		}
	}

	private ReentrantLock getLock(String name) {
		try {
			return locks.get(name, () -> new ReentrantLock());
		} catch (ExecutionException e) {
			log.error("getLock error", e);
		}
		return new ReentrantLock();
	}
}
