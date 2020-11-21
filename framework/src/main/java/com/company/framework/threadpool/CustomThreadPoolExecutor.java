package com.company.framework.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.company.framework.filter.MdcUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "LOG_THREADPOOL")
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	/**
	 * 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
	 */
	private ThreadLocal<Long> timetl = new ThreadLocal<>();

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
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
		MdcUtil.remove();
	}

	@Override
	public void execute(Runnable command) {
		super.execute(new MdcWrappedRunnable(command));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(new MdcWrappedRunnable(task));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return super.submit(new MdcWrappedRunnable(task), result);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(new MdcWrappedCallable<T>(task));
	}

	private static class MdcWrappedCallable<T> implements Callable<T> {
		private final Callable<T> target;
		private final String traceId;

		public MdcWrappedCallable(Callable<T> target) {
			this.target = target;
			this.traceId = MdcUtil.get();
		}

		@Override
		public T call() throws Exception {
			MdcUtil.put(traceId);
			return target.call();
		}
	}

	private static class MdcWrappedRunnable implements Runnable {
		private final Runnable target;
		private final String traceId;

		public MdcWrappedRunnable(Runnable target) {
			this.target = target;
			this.traceId = MdcUtil.get();
		}

		@Override
		public void run() {
			MdcUtil.put(traceId);
			target.run();
		}
	}
}
