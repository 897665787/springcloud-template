package com.company.framework.redis.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.company.framework.context.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistributeLockUtils {
	private static RedissonClient redisson = SpringContextUtil.getBean(RedissonClient.class);

	private DistributeLockUtils() {
	}

	public static <V> V doInDistributeLock(String key, Exe<V> exe) {
		RLock lock = redisson.getLock(key);
		try {
			lock.lock();
			return exe.execute();
		} catch (Exception e) {
			log.error("doInDistributeLock error,key:{}", key, e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	public static <V> V doInDistributeLockThrow(String key, Exe<V> exe) {
		RLock lock = redisson.getLock(key);
		try {
			lock.lock();
			return exe.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			lock.unlock();
		}
	}

	public static <V> V tryDoInDistributeLock(String key, Exe<V> exe) {
		RLock lock = redisson.getLock(key);
		boolean tryLock = lock.tryLock();
		if (tryLock) {
			try {
				return exe.execute();
			} catch (Exception e) {
				log.error("tryDoInDistributeLock error,key:{}", key, e);
			} finally {
				lock.unlock();
			}
		} else {
			log.info("unable to get key:{}", key);
		}
		return null;
	}

	public interface Exe<V> {
		V execute();
	}
}
