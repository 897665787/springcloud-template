package com.company.system.scheduler;

/**
 * 定时任务调度器
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.company.system.entity.SysJob;
import com.company.system.entity.SysJobLog;
import com.company.system.mapper.SysJobMapper;
import com.company.system.service.SysJobLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务调度器
 * <p>
 * 使用 Spring 的 ThreadPoolTaskScheduler + CronTrigger 实现，任务定义存储在 sys_job，
 * 应用启动时自动装载启用中的任务。invokeTarget 格式：beanName.methodName
 */
@Slf4j
@Component
public class SysJobScheduler implements ApplicationRunner {

	// 任务执行状态-成功
	private static final String LOG_STATUS_SUCCESS = "0";

	// 任务执行状态-失败
	private static final String LOG_STATUS_FAIL = "1";

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private SysJobMapper sysJobMapper;
	@Autowired
	private SysJobLogService sysJobLogService;

	// 运行中的调度任务
	private final Map<Integer, ScheduledFuture<?>> scheduledMap = new ConcurrentHashMap<>();

	// 调度线程池
	private final ThreadPoolTaskScheduler taskScheduler;

	public SysJobScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		// 调度线程数
		scheduler.setPoolSize(4);
		// 线程名前缀
		scheduler.setThreadNamePrefix("sys-job-");
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.initialize();
		this.taskScheduler = scheduler;
	}

	/**
	 * 应用启动后装载启用中的任务
	 */
	@Override
	public void run(ApplicationArguments args) {
		try {
			sysJobMapper.selectList(null).stream()
					.filter(job -> SysJob.STATUS_NORMAL.equals(job.getStatus()))
					.forEach(this::refresh);
			log.info("定时任务装载完成，运行中任务数:{}", scheduledMap.size());
		} catch (Exception e) {
			log.error("定时任务装载失败", e);
		}
	}

	/**
	 * 刷新任务调度（先取消再按最新配置注册）
	 */
	public void refresh(SysJob job) {
		if (job == null || job.getId() == null) {
			return;
		}
		cancel(job.getId());
		if (!SysJob.STATUS_NORMAL.equals(job.getStatus()) || StringUtils.isBlank(job.getCronExpression())) {
			return;
		}
		try {
			CronTrigger trigger = new CronTrigger(job.getCronExpression());
			ScheduledFuture<?> future = taskScheduler.schedule(() -> execute(job.getId()), trigger);
			if (future != null) {
				scheduledMap.put(job.getId(), future);
			}
		} catch (Exception e) {
			log.error("定时任务注册失败,id:{},cron:{}", job.getId(), job.getCronExpression(), e);
		}
	}

	/**
	 * 取消任务调度
	 */
	public void cancel(Integer jobId) {
		ScheduledFuture<?> future = scheduledMap.remove(jobId);
		if (future != null) {
			future.cancel(false);
		}
	}

	/**
	 * 立即执行一次
	 */
	public void runOnce(Integer jobId) {
		execute(jobId);
	}

	/**
	 * 执行任务并记录日志
	 */
	private void execute(Integer jobId) {
		SysJob job = sysJobMapper.selectById(jobId);
		if (job == null) {
			return;
		}
		SysJobLog jobLog = new SysJobLog();
		jobLog.setJobName(job.getJobName());
		jobLog.setJobGroup(job.getJobGroup());
		jobLog.setInvokeTarget(job.getInvokeTarget());
		jobLog.setStartTime(LocalDateTime.now());

		try {
			invoke(job.getInvokeTarget());
			jobLog.setStatus(LOG_STATUS_SUCCESS);
			jobLog.setJobMessage("执行成功");
		} catch (Exception e) {
			log.error("定时任务执行失败,id:{},target:{}", jobId, job.getInvokeTarget(), e);
			jobLog.setStatus(LOG_STATUS_FAIL);
			jobLog.setJobMessage("执行失败:" + e.getMessage());
			jobLog.setExceptionInfo(StringUtils.left(String.valueOf(e), 2000));
		}
		jobLog.setEndTime(LocalDateTime.now());
		sysJobLogService.save(jobLog);
	}

	/**
	 * 反射调用目标方法，格式：beanName.methodName
	 */
	private void invoke(String invokeTarget) throws Exception {
		if (StringUtils.isBlank(invokeTarget) || !invokeTarget.contains(".")) {
			throw new IllegalArgumentException("调用目标格式错误，应为 beanName.methodName");
		}
		String beanName = StringUtils.substringBefore(invokeTarget, ".");
		String methodName = StringUtils.substringAfter(invokeTarget, ".");

		Object bean = applicationContext.getBean(beanName);
		bean.getClass().getMethod(methodName).invoke(bean);
	}

	/**
	 * 应用关闭时停止所有调度
	 */
	@PreDestroy
	public void shutdown() {
		scheduledMap.values().forEach(future -> future.cancel(false));
		scheduledMap.clear();
		taskScheduler.shutdown();
	}
}
