package com.company.adminapi.controller;

/**
 * 岗位信息（管理后台）
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
import com.company.adminapi.excel.SysPostExcel;
import com.company.adminapi.feign.client.SysPostFeign;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysPostReq;
import com.company.system.api.response.SysPostResp;

/**
 * 岗位信息（管理后台）
 */
@Validated
@RestController
@RequestMapping("/sysPost")
public class SysPostController {

	@Autowired
	private SysPostFeign sysPostFeign;

	/**
	 * 分页查询岗位
	 */
	@RequirePermissions("system:post:list")
	@GetMapping("/page")
	public PageResp<SysPostResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String postCode, String postName, String status) {
		return sysPostFeign.page(current, size, postCode, postName, status);
	}

	/**
	 * 查询岗位列表
	 */
	@RequirePermissions("system:post:list")
	@GetMapping("/list")
	public List<SysPostResp> list(String postCode, String postName, String status) {
		return sysPostFeign.list(postCode, postName, status);
	}

	/**
	 * 查询岗位详情
	 */
	@RequirePermissions("system:post:list")
	@GetMapping("/query")
	public SysPostResp query(@NotNull Integer id) {
		return sysPostFeign.query(id);
	}

	/**
	 * 新增岗位
	 */
	@OperationLog(title = "岗位信息保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:post:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysPostReq sysPostReq) {
		return sysPostFeign.save(sysPostReq);
	}

	/**
	 * 修改岗位
	 */
	@OperationLog(title = "岗位信息更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:post:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysPostReq sysPostReq) {
		return sysPostFeign.update(sysPostReq);
	}

	/**
	 * 删除岗位
	 */
	@OperationLog(title = "岗位信息删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:post:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysPostFeign.remove(req);
	}

	/**
	 * 导出岗位
	 */
	@OperationLog(title = "岗位信息导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:post:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String postCode, String postName, String status) {
		List<SysPostResp> listResp = sysPostFeign.list(postCode, postName, status);
		List<SysPostExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysPostExcel.class);
		ExcelUtil.write2httpResponse(response, "岗位信息", SysPostExcel.class, excelList);
	}
}
