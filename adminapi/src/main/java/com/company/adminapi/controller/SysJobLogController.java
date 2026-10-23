package com.company.adminapi.controller;

/**
 * 定时任务日志（管理后台）
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
import com.company.adminapi.excel.SysJobLogExcel;
import com.company.adminapi.feign.client.SysJobLogFeign;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.SysJobLogResp;

/**
 * 定时任务日志（管理后台）
 */
@Validated
@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController {

	@Autowired
	private SysJobLogFeign sysJobLogFeign;

	/**
	 * 分页查询任务日志
	 */
	@RequirePermissions("monitor:job:query")
	@GetMapping("/page")
	public PageResp<SysJobLogResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String jobName, String jobGroup, String status) {
		return sysJobLogFeign.page(current, size, jobName, jobGroup, status);
	}

	/**
	 * 删除任务日志
	 */
	@OperationLog(title = "定时任务日志删除", businessType = BusinessType.DELETE)
	@RequirePermissions("monitor:job:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysJobLogFeign.remove(req);
	}

	/**
	 * 清空任务日志
	 */
	@OperationLog(title = "定时任务日志清空", businessType = BusinessType.CLEAN)
	@RequirePermissions("monitor:job:remove")
	@PostMapping("/clean")
	public Boolean clean() {
		return sysJobLogFeign.clean();
	}

	/**
	 * 导出任务日志
	 */
	@OperationLog(title = "定时任务日志导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("monitor:job:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String jobName, String jobGroup, String status) {
		List<SysJobLogResp> listResp = sysJobLogFeign.page(1L, Long.MAX_VALUE, jobName, jobGroup, status).getList();
		List<SysJobLogExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysJobLogExcel.class);
		ExcelUtil.write2httpResponse(response, "定时任务日志", SysJobLogExcel.class, excelList);
	}
}
