package com.company.system.service;

/**
 * 定时任务服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysJob;
import com.company.system.mapper.SysJobMapper;
import com.company.system.scheduler.SysJobScheduler;

/**
 * 定时任务服务
 */
@Service
public class SysJobService extends ServiceImpl<SysJobMapper, SysJob> implements IService<SysJob> {

	@Autowired
	private SysJobScheduler sysJobScheduler;

	/**
	 * 新增任务并注册调度
	 */
	public boolean saveAndSchedule(SysJob sysJob) {
		boolean success = this.save(sysJob);
		if (success) {
			sysJobScheduler.refresh(sysJob);
		}
		return success;
	}

	/**
	 * 修改任务并刷新调度
	 */
	public boolean updateAndSchedule(SysJob sysJob) {
		boolean success = this.updateById(sysJob);
		if (success) {
			sysJobScheduler.refresh(this.getById(sysJob.getId()));
		}
		return success;
	}

	/**
	 * 删除任务并取消调度
	 */
	public boolean removeAndCancel(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return true;
		}
		ids.forEach(sysJobScheduler::cancel);
		return this.removeBatchByIds(ids);
	}

	/**
	 * 修改任务状态（同步刷新调度）
	 */
	public boolean changeStatus(Integer id, String status) {
		SysJob sysJob = new SysJob();
		sysJob.setId(id);
		sysJob.setStatus(status);
		return this.updateAndSchedule(sysJob);
	}

	/**
	 * 立即执行一次
	 */
	public boolean runOnce(Integer id) {
		SysJob sysJob = this.getById(id);
		if (sysJob == null) {
			return false;
		}
		sysJobScheduler.runOnce(id);
		return true;
	}
}
