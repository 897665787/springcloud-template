package com.company.framework.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class ThreadPoolAutoConfig {

	@Bean
	ThreadPoolExecutor threadPoolExecutor() {
		int corePoolSize = 4;
		int maximumPoolSize = 5;
		long keepAliveTime = 1L;
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
		ThreadPoolExecutor threadPoolExecutor = new CustomThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, workQueue, new CustomDefaultThreadFactory(), new CustomCallerRunsPolicy());

		return threadPoolExecutor;
	}

	@Slf4j
	static class CustomDefaultThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		CustomDefaultThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			log.info("factory new thread {}", threadNumber.get());
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}

	@Slf4j
	public static class CustomCallerRunsPolicy implements RejectedExecutionHandler {
		public CustomCallerRunsPolicy() {
		}

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			long start = System.currentTimeMillis();
			if (!e.isShutdown()) {
				r.run();
			}
			long diff = System.currentTimeMillis() - start;
			log.info(
					"rejected execution,duration:{} ms,poolSize:{},active:{},completedTaskCount:{},taskCount:{},queue:{},largestPoolSize:{}",
					diff, e.getPoolSize(), e.getActiveCount(), e.getCompletedTaskCount(), e.getTaskCount(),
					e.getQueue().size(), e.getLargestPoolSize());
		}
	}
}
