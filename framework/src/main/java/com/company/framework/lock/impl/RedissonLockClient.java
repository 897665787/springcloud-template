package com.company.framework.lock.impl;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import com.company.framework.lock.LockClient;

public class RedissonLockClient implements LockClient {

	private RedissonClient redisson = null;

	public RedissonLockClient(RedisProperties redisProperties) {
		Config config = new Config();
		String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();

		SingleServerConfig serverConfig = config.useSingleServer().setAddress(address)
				.setConnectTimeout((int) redisProperties.getTimeout().toMillis())
				.setDatabase(redisProperties.getDatabase());
		if (StringUtils.isNotBlank(redisProperties.getPassword())) {
			serverConfig.setPassword(redisProperties.getPassword());
		}
		this.redisson = Redisson.create(config);
	}

	@Override
	public <V> V doInLock(String name, Supplier<V> supplier) {
		RLock lock = redisson.getLock(name);
		try {
			lock.lock();
			return supplier.get();
		} finally {
			lock.unlock();
		}
	}
}
