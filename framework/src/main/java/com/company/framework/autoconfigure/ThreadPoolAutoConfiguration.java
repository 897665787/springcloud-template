package com.company.framework.autoconfigure;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.company.framework.threadpool.CustomThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(prefix = "template", name = "threadpool.maxPoolSize")
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolAutoConfiguration {

	@Bean(destroyMethod = "destroy")
	ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties properties) {
		ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
		// 设置线程池核心容量
		executor.setCorePoolSize(properties.getCorePoolSize());
		// 设置线程池最大容量
		executor.setMaxPoolSize(properties.getMaxPoolSize());
		// 设置任务队列长度
		executor.setQueueCapacity(properties.getQueueCapacity());
		// 设置线程超时时间
		executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
		// 设置线程名称前缀
		executor.setThreadNamePrefix(properties.getThreadNamePrefix());
		// 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(properties.getWaitForTasksToCompleteOnShutdown());
		// 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
		executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
		// 设置任务丢弃后的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}
	/*
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
*/
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
