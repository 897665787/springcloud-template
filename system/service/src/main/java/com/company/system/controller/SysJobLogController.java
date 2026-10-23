package com.company.system.controller;

/**
 * 定时任务日志
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
import com.company.system.api.interfaces.SysJobLogApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.SysJobLogResp;
import com.company.system.entity.SysJobLog;
import com.company.system.service.SysJobLogService;

/**
 * 定时任务日志
 */
@RestController
@RequestMapping("/sysJobLog")
public class SysJobLogController implements SysJobLogApi {

	@Autowired
	private SysJobLogService sysJobLogService;

	/**
	 * 构建查询条件
	 */
	private QueryWrapper<SysJobLog> toQueryWrapper(String jobName, String jobGroup, String status) {
		QueryWrapper<SysJobLog> queryWrapper = new QueryWrapper<>();
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
	public PageResp<SysJobLogResp> page(Long current, Long size, String jobName, String jobGroup, String status) {
		QueryWrapper<SysJobLog> queryWrapper = toQueryWrapper(jobName, jobGroup, status);
		long count = sysJobLogService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysJobLog> list = sysJobLogService.list(PageDTO.of(current, size), queryWrapper);

		List<SysJobLogResp> respList = PropertyUtils.copyArrayProperties(list, SysJobLogResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return true;
		}
		return sysJobLogService.removeBatchByIds(req.getIdList());
	}

	@Override
	public Boolean clean() {
		return sysJobLogService.remove(new QueryWrapper<>());
	}
}
