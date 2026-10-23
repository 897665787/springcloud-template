package com.company.adminapi.controller;

/**
 * 通知公告（管理后台）
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

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
import com.company.adminapi.excel.SysNoticeExcel;
import com.company.adminapi.feign.client.SysNoticeFeign;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysNoticeReq;
import com.company.system.api.response.SysNoticeResp;

/**
 * 通知公告（管理后台）
 */
@Validated
@RestController
@RequestMapping("/sysNotice")
public class SysNoticeController {

	@Autowired
	private SysNoticeFeign sysNoticeFeign;

	/**
	 * 分页查询公告
	 */
	@RequirePermissions("system:notice:list")
	@GetMapping("/page")
	public PageResp<SysNoticeResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String noticeTitle, String noticeType, String createBy, String status) {
		return sysNoticeFeign.page(current, size, noticeTitle, noticeType, createBy, status);
	}

	/**
	 * 查询公告详情
	 */
	@RequirePermissions("system:notice:list")
	@GetMapping("/query")
	public SysNoticeResp query(@NotNull Integer id) {
		return sysNoticeFeign.query(id);
	}

	/**
	 * 新增公告
	 */
	@OperationLog(title = "通知公告保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:notice:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysNoticeReq sysNoticeReq) {
		return sysNoticeFeign.save(sysNoticeReq);
	}

	/**
	 * 修改公告
	 */
	@OperationLog(title = "通知公告更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:notice:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysNoticeReq sysNoticeReq) {
		return sysNoticeFeign.update(sysNoticeReq);
	}

	/**
	 * 删除公告
	 */
	@OperationLog(title = "通知公告删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:notice:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysNoticeFeign.remove(req);
	}
}
