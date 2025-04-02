package com.company.adminapi.controller;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.excel.SysConfigExcel;
import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.system.api.feign.SysConfigFeign;
import com.company.system.api.request.SysConfigReq;
import com.company.system.api.response.SysConfigResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

	@Autowired
	private SysConfigFeign sysConfigFeign;

	@RequirePermissions("system:sysConfig:query")
	@GetMapping("/page")
	public Result<PageResp<SysConfigResp>> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, String name, String code, String value, String configRemark) {
		return sysConfigFeign.page(current, size, name, code, value, configRemark);
	}

	@RequirePermissions("system:sysConfig:query")
	@GetMapping("/query")
	public Result<SysConfigResp> query(@NotNull Integer id) {
		return sysConfigFeign.query(id);
	}

    @OperationLog(title = "参数配置保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:sysConfig:save")
	@PostMapping("/save")
	public Result<Boolean> save(@RequestBody SysConfigReq sysConfigReq) {
		return sysConfigFeign.save(sysConfigReq);
	}

    @OperationLog(title = "参数配置更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:sysConfig:update")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody SysConfigReq sysConfigReq) {
		return sysConfigFeign.update(sysConfigReq);
	}

    @OperationLog(title = "参数配置删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:sysConfig:remove")
	@PostMapping("/remove")
	public Result<Boolean> remove(@RequestBody RemoveReq<Integer> req) {
		return sysConfigFeign.remove(req);
	}

	@OperationLog(title = "参数配置导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:sysConfig:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String name, String code, String value, String configRemark) {
		List<SysConfigResp> listResp = sysConfigFeign.list(name, code, value, configRemark).dataOrThrow();
		List<SysConfigExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysConfigExcel.class);
		ExcelUtil.write2httpResponse(response, "参数配置", SysConfigExcel.class, excelList);
	}

}