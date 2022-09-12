package com.company.user.database.datasource;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SourceName {
	String MASTER = "master";
	String SLAVE1 = "slave1";
	String SLAVE2 = "slave2";

	// 从库集合
	List<String> SLAVE_NAMES = Lists.newArrayList(SLAVE1, SLAVE2);

	AtomicInteger INDEX = new AtomicInteger(0);

	@AllArgsConstructor
	public enum Slave {
		// 从库
		SLAVE1(() -> SourceName.SLAVE1), SLAVE2(() -> SourceName.SLAVE2),
		// 所有从库参与
		SLAVE_POLLING(() -> {
			// 使用轮询算法
			return SLAVE_NAMES.get(Math.abs(INDEX.getAndAdd(1)) % SLAVE_NAMES.size());
		}), SLAVE_RANDOM(() -> {
			// 使用随机算法
			return SLAVE_NAMES.get(ThreadLocalRandom.current().nextInt(SLAVE_NAMES.size()));
		});

		@Getter
		private Supplier<String> value;
	}
}