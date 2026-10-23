package com.company.system.controller;

/**
 * 定时任务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;

import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.interfaces.SysJobApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysJobReq;
import com.company.system.api.response.SysJobResp;
import com.company.system.entity.SysJob;
import com.company.system.service.SysJobService;

/**
 * 定时任务
 */
@RestController
@RequestMapping("/sysJob")
public class SysJobController implements SysJobApi {

	@Autowired
	private SysJobService sysJobService;

	/**
	 * 构建查询条件
	 */
	private QueryWrapper<SysJob> toQueryWrapper(String jobName, String jobGroup, String status) {
		QueryWrapper<SysJob> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(jobName)) {
			queryWrapper.like("job_name", jobName);
		}
		if (StringUtils.isNotBlank(jobGroup)) {
			queryWrapper.eq("job_group", jobGroup);
		}
		if (StringUtils.isNotBlank(status)) {
			queryWrapper.eq("status", status);
		}
		return queryWrapper;
	}

	@Override
	public PageResp<SysJobResp> page(Long current, Long size, String jobName, String jobGroup, String status) {
		QueryWrapper<SysJob> queryWrapper = toQueryWrapper(jobName, jobGroup, status);
		long count = sysJobService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysJob> list = sysJobService.list(PageDTO.of(current, size), queryWrapper);

		List<SysJobResp> respList = PropertyUtils.copyArrayProperties(list, SysJobResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public SysJobResp query(Integer id) {
		SysJob sysJob = sysJobService.getById(id);
		return PropertyUtils.copyProperties(sysJob, SysJobResp.class);
	}

	@Override
	public Boolean save(SysJobReq sysJobReq) {
		SysJob sysJob = PropertyUtils.copyProperties(sysJobReq, SysJob.class);
		return sysJobService.saveAndSchedule(sysJob);
	}

	@Override
	public Boolean update(SysJobReq sysJobReq) {
		SysJob sysJob = PropertyUtils.copyProperties(sysJobReq, SysJob.class);
		return sysJobService.updateAndSchedule(sysJob);
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysJobService.removeAndCancel(req.getIdList());
	}

	@Override
	public Boolean changeStatus(@RequestBody SysJobReq sysJobReq) {
		return sysJobService.changeStatus(sysJobReq.getId(), sysJobReq.getStatus());
	}

	@Override
	public Boolean run(@RequestBody SysJobReq sysJobReq) {
		return sysJobService.runOnce(sysJobReq.getId());
	}
}
