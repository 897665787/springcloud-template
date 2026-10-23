package com.company.adminapi.controller;

/**
 * 定时任务（管理后台）
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.excel.SysJobExcel;
import com.company.adminapi.feign.client.SysJobFeign;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysJobReq;
import com.company.system.api.response.SysJobResp;

/**
 * 定时任务（管理后台）
 */
@Validated
@RestController
@RequestMapping("/monitor/job")
public class SysJobController {

	@Autowired
	private SysJobFeign sysJobFeign;

	/**
	 * 分页查询任务
	 */
	@RequirePermissions("monitor:job:list")
	@GetMapping("/page")
	public PageResp<SysJobResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String jobName, String jobGroup, String status) {
		return sysJobFeign.page(current, size, jobName, jobGroup, status);
	}

	/**
	 * 查询任务详情
	 */
	@RequirePermissions("monitor:job:query")
	@GetMapping("/query")
	public SysJobResp query(@NotNull Integer id) {
		return sysJobFeign.query(id);
	}

	/**
	 * 新增任务
	 */
	@OperationLog(title = "定时任务保存", businessType = BusinessType.INSERT)
	@RequirePermissions("monitor:job:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysJobReq sysJobReq) {
		return sysJobFeign.save(sysJobReq);
	}

	/**
	 * 修改任务
	 */
	@OperationLog(title = "定时任务更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("monitor:job:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysJobReq sysJobReq) {
		return sysJobFeign.update(sysJobReq);
	}

	/**
	 * 删除任务
	 */
	@OperationLog(title = "定时任务删除", businessType = BusinessType.DELETE)
	@RequirePermissions("monitor:job:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysJobFeign.remove(req);
	}

	/**
	 * 修改任务状态
	 */
	@OperationLog(title = "定时任务状态修改", businessType = BusinessType.UPDATE)
	@RequirePermissions("monitor:job:changeStatus")
	@PostMapping("/changeStatus")
	public Boolean changeStatus(@RequestBody SysJobReq sysJobReq) {
		return sysJobFeign.changeStatus(sysJobReq);
	}

	/**
	 * 立即执行一次
	 */
	@OperationLog(title = "定时任务执行", businessType = BusinessType.OTHER)
	@RequirePermissions("monitor:job:changeStatus")
	@PostMapping("/run")
	public Boolean run(@RequestBody SysJobReq sysJobReq) {
		return sysJobFeign.run(sysJobReq);
	}

	/**
	 * 导出任务
	 */
	@OperationLog(title = "定时任务导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("monitor:job:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String jobName, String jobGroup, String status) {
		List<SysJobResp> listResp = sysJobFeign.page(1L, Long.MAX_VALUE, jobName, jobGroup, status).getList();
		List<SysJobExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysJobExcel.class);
		ExcelUtil.write2httpResponse(response, "定时任务", SysJobExcel.class, excelList);
	}
}
