package com.company.adminapi.controller;

/**
 * 代码生成（管理后台）
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.feign.client.SysGenFeign;
import com.company.common.response.PageResp;
import com.company.framework.globalresponse.ExceptionUtil;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysGenTableReq;
import com.company.system.api.response.SysGenTableResp;
import com.feiniaojin.gracefulresponse.api.ExcludeFromGracefulResponse;

/**
 * 代码生成（管理后台）
 * <p>
 * 代码的实际运行位置在 template-code-generate 服务，这里仅做门面转发。
 */
@Validated
@RestController
@RequestMapping("/tool/gen")
public class SysGenController {

	// zip 文件后缀
	private static final String ZIP_SUFFIX = ".zip";

	@Autowired
	private SysGenFeign sysGenFeign;

	/**
	 * 分页查询已导入的生成配置
	 */
	@RequirePermissions("tool:gen:list")
	@GetMapping("/list")
	public PageResp<SysGenTableResp> list(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String tableName, String tableComment) {
		return sysGenFeign.page(current, size, tableName, tableComment);
	}

	/**
	 * 分页查询数据库中可导入的表
	 */
	@RequirePermissions("tool:gen:list")
	@GetMapping("/db/list")
	public PageResp<Map<String, Object>> dbList(@NotNull @Min(value = 1) Long current, @NotNull Long size,
			String tableName, String tableComment) {
		return sysGenFeign.dbPage(current, size, tableName, tableComment);
	}

	/**
	 * 查询生成配置详情
	 */
	@RequirePermissions("tool:gen:query")
	@GetMapping("/query")
	public SysGenTableResp query(@NotNull Integer id) {
		return sysGenFeign.query(id);
	}

	/**
	 * 保存生成配置
	 */
	@OperationLog(title = "代码生成配置更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("tool:gen:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysGenTableReq req) {
		return sysGenFeign.update(req);
	}

	/**
	 * 导入表
	 */
	@OperationLog(title = "代码生成导入表", businessType = BusinessType.IMPORT)
	@RequirePermissions("tool:gen:import")
	@PostMapping("/importTable")
	public Boolean importTable(@RequestParam("tables") String tables) {
		return sysGenFeign.importTable(tables);
	}

	/**
	 * 执行建表语句
	 */
	@OperationLog(title = "代码生成创建表", businessType = BusinessType.INSERT)
	@RequirePermissions("tool:gen:create")
	@PostMapping("/createTable")
	public Boolean createTable(@RequestParam("sql") String sql) {
		return sysGenFeign.createTable(sql);
	}

	/**
	 * 删除生成配置
	 */
	@OperationLog(title = "代码生成配置删除", businessType = BusinessType.DELETE)
	@RequirePermissions("tool:gen:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysGenFeign.remove(req);
	}

	/**
	 * 预览生成的代码
	 */
	@RequirePermissions("tool:gen:preview")
	@GetMapping("/preview")
	public Map<String, String> preview(@NotNull Integer id) {
		return sysGenFeign.preview(id);
	}

	/**
	 * 同步数据库表结构
	 */
	@OperationLog(title = "代码生成同步表结构", businessType = BusinessType.UPDATE)
	@RequirePermissions("tool:gen:edit")
	@PostMapping("/synchDb")
	public Boolean synchDb(@RequestParam("tableName") String tableName) {
		return sysGenFeign.synchDb(tableName);
	}

	/**
	 * 生成代码到自定义路径
	 */
	@OperationLog(title = "代码生成到自定义路径", businessType = BusinessType.CODE_GEN)
	@RequirePermissions("tool:gen:code")
	@GetMapping("/genCode")
	public Boolean genCode(@RequestParam("tableName") String tableName) {
		return sysGenFeign.genCode(tableName);
	}

	/**
	 * 下载代码压缩包
	 */
	@OperationLog(title = "代码生成下载", businessType = BusinessType.CODE_GEN, isSaveResponseData = false)
	@RequirePermissions("tool:gen:code")
	@ExcludeFromGracefulResponse
	@GetMapping("/batchGenCode")
	public void batchGenCode(HttpServletResponse response, @RequestParam("tables") String tables) throws IOException {
		if (StringUtils.isBlank(tables)) {
			ExceptionUtil.throwException("请选择要生成的表");
		}
		String base64 = sysGenFeign.downloadBase64(tables);
		if (StringUtils.isBlank(base64)) {
			ExceptionUtil.throwException("生成代码失败");
		}
		byte[] zipBytes = Base64.getDecoder().decode(base64);

		// 多个表时使用 ruoyi.zip，与若依前端保持一致
		List<String> tableList = Arrays.asList(tables.split(","));
		String fileName = tableList.size() == 1 ? tableList.get(0).trim() + ZIP_SUFFIX : "ruoyi" + ZIP_SUFFIX;

		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		try (OutputStream outputStream = response.getOutputStream()) {
			outputStream.write(zipBytes);
			outputStream.flush();
		}
	}
}
