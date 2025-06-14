package com.company.framework.threadpool;

import com.company.framework.trace.TraceManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j(topic = "LOG_THREADPOOL")
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
	private TraceManager traceManager;

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
								   BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, TraceManager traceManager) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		this.traceManager = traceManager;
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
								   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, TraceManager traceManager) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.traceManager = traceManager;
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
								   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, TraceManager traceManager) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		this.traceManager = traceManager;
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
								   BlockingQueue<Runnable> workQueue, TraceManager traceManager) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.traceManager = traceManager;
	}

	/**
	 * 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
	 */
	private ThreadLocal<Long> timetl = new ThreadLocal<>();

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if (r instanceof TraceRunnable) {
			TraceRunnable wr = (TraceRunnable) r;
			traceManager.put(wr.getTraceId());
		}
		timetl.set(System.currentTimeMillis());
		super.beforeExecute(t, r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		Long start = timetl.get();
		timetl.remove();
		long diff = System.currentTimeMillis() - start;
		// 统计任务耗时、初始线程数、正在执行的任务数量、 已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数
		log.info("duration:{} ms,poolSize:{},active:{},completedTaskCount:{},taskCount:{},queue:{},largestPoolSize:{}",
				diff, this.getPoolSize(), this.getActiveCount(), this.getCompletedTaskCount(), this.getTaskCount(),
				this.getQueue().size(), this.getLargestPoolSize());
		if (r instanceof TraceRunnable) {
			traceManager.remove();
		}
	}

	@Override
	public void execute(Runnable command) {
		super.execute(new TraceRunnable(command, traceManager.get()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return super.submit(task, result);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(task);
	}

	private static class TraceRunnable implements Runnable {
		private final Runnable target;
		@Getter
		private String traceId;

		public TraceRunnable(Runnable target, String traceId) {
			this.target = target;
			this.traceId = traceId;
		}

		@Override
		public void run() {
			target.run();
		}
	}
}
