package com.company.framework.redis.redisson;

import java.util.Stack;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.company.framework.context.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistributeLockUtils {
	private static ThreadLocal<Stack<RLock>> LOCK_INSTACE = new ThreadLocal<Stack<RLock>>() {
		@Override
		protected Stack<RLock> initialValue() {
			return new Stack<>();
		}
	};
	private static RedissonClient redisson = SpringContextUtil.getBean(RedissonClient.class);

	private static boolean tryLock(String key) {
		RLock lock = redisson.getLock(key);
		boolean tryLock = lock.tryLock();
		if (tryLock) {
			LOCK_INSTACE.get().push(lock);
		}
		return tryLock;
	}

	private static void lock(String key) {
		RLock lock = redisson.getLock(key);
		lock.lock();
		LOCK_INSTACE.get().push(lock);
	}

	private static void unlock(String key) {
		RLock lock = LOCK_INSTACE.get().pop();
		if (lock == null) {
			return;
		}
		lock.unlock();
	}

	public static <V> V doInDistributeLock(String key, Exe<V> exe) {
		try {
			lock(key);
			return exe.execute();
		} catch (Exception e) {
			log.error("doInDistributeLock error,key:{}", key, e);
		} finally {
			unlock(key);
		}
		return null;
	}
	
	public static <V> V doInDistributeLockThrow(String key, Exe<V> exe) {
		try {
			lock(key);
			return exe.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			unlock(key);
		}
	}

	public static <V> V tryDoInDistributeLock(String key, Exe<V> exe) {
		if (tryLock(key)) {
			try {
				return exe.execute();
			} catch (Exception e) {
				log.error("tryDoInDistributeLock error,key:{}", key, e);
			} finally {
				unlock(key);
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
