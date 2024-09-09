package com.company.admin.controller;

import java.util.List;
import java.math.BigDecimal;

import com.company.common.request.RemoveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.admin.annotation.OperationLog;
import com.company.admin.annotation.RequirePermissions;
import com.company.admin.easyexcel.ExcelUtil;
import com.company.admin.enums.OperationLogEnum.BusinessType;
import com.company.admin.excel.{ModelName}Excel;
import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.{module}.api.feign.{ModelName}Feign;
import com.company.{module}.api.request.{ModelName}Req;
import com.company.{module}.api.response.{ModelName}Resp;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/{modelName}")
public class {ModelName}Controller {

	@Autowired
	private {ModelName}Feign {modelName}Feign;

	@RequirePermissions("{module}:{modelName}:query")
	@GetMapping("/page")
	public Result<PageResp<{ModelName}Resp>> page({page_column_field_feign}) {
		return {modelName}Feign.page({page_column_field});
	}

	@RequirePermissions("{module}:{modelName}:query")
	@GetMapping("/query")
	public Result<{ModelName}Resp> query(@NotNull Integer id) {
		return {modelName}Feign.query(id);
	}

    @OperationLog(title = "{tableComment}保存", businessType = BusinessType.INSERT)
	@RequirePermissions("{module}:{modelName}:save")
	@PostMapping("/save")
	public Result<Boolean> save(@RequestBody {ModelName}Req {modelName}Req) {
		return {modelName}Feign.save({modelName}Req);
	}

    @OperationLog(title = "{tableComment}更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("{module}:{modelName}:update")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody {ModelName}Req {modelName}Req) {
		return {modelName}Feign.update({modelName}Req);
	}

    @OperationLog(title = "{tableComment}删除", businessType = BusinessType.DELETE)
	@RequirePermissions("{module}:{modelName}:remove")
	@PostMapping("/remove")
	public Result<Boolean> remove(@RequestBody RemoveReq<Integer> req) {
		return {modelName}Feign.remove(req);
	}

	@OperationLog(title = "{tableComment}导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("{module}:{modelName}:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, {column_field_feign}) {
		List<{ModelName}Resp> listResp = {modelName}Feign.list({column_field}).dataOrThrow();
		List<{ModelName}Excel> excelList = PropertyUtils.copyArrayProperties(listResp, {ModelName}Excel.class);
		ExcelUtil.write2httpResponse(response, "{tableComment}", {ModelName}Excel.class, excelList);
	}

}