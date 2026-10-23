package com.company.codegenerate.controller;

/**
 * 代码生成服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.codegenerate.entity.GenTable;
import com.company.codegenerate.generator.CodeGeneratorService;
import com.company.codegenerate.service.GenTableService;
import com.company.codegenerate.service.GenTemplateService;
import com.company.common.response.PageResp;
import com.company.framework.globalresponse.ExceptionUtil;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.interfaces.SysGenApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysGenTableReq;
import com.company.system.api.response.SysGenTableResp;

/**
 * 代码生成服务
 * <p>
 * 实现 system-api 中的 {@link SysGenApi} 契约，由 adminapi 通过 Feign 调用。
 */
@RestController
@RequestMapping("/sysGen")
public class GenApiController implements SysGenApi {

	// 自定义路径未配置时使用当前工作目录
	private static final String DEFAULT_GEN_PATH = System.getProperty("user.dir");

	@Autowired
	private GenTableService genTableService;
	@Autowired
	private GenTemplateService genTemplateService;
	@Autowired
	private CodeGeneratorService codeGeneratorService;

	/**
	 * 分页查询已导入的生成配置
	 */
	@Override
	public PageResp<SysGenTableResp> page(Long current, Long size, String tableName, String tableComment) {
		List<GenTable> tableList = genTableService.list(tableName, tableComment);
		List<SysGenTableResp> respList = PropertyUtils.copyArrayProperties(tableList, SysGenTableResp.class);
		return slice(respList, current, size);
	}

	/**
	 * 分页查询数据库中可导入的表
	 */
	@Override
	public PageResp<Map<String, Object>> dbPage(Long current, Long size, String tableName, String tableComment) {
		return slice(genTableService.listDbTable(tableName, tableComment), current, size);
	}

	/**
	 * 内存分页（生成配置/可导入表数量很少，无需数据库分页）
	 */
	private <T> PageResp<T> slice(List<T> list, Long current, Long size) {
		long pageNo = current == null || current < 1 ? 1 : current;
		long pageSize = size == null || size < 1 ? 10 : size;
		int fromIndex = (int) Math.min((pageNo - 1) * pageSize, list.size());
		int toIndex = (int) Math.min(fromIndex + pageSize, list.size());
		return PageResp.of((long) list.size(), list.subList(fromIndex, toIndex));
	}

	/**
	 * 查询生成配置详情（含字段）
	 */
	@Override
	public SysGenTableResp query(Integer id) {
		GenTable genTable = genTableService.getByTableId(id);
		if (genTable == null) {
			ExceptionUtil.throwException("生成配置不存在");
		}
		SysGenTableResp resp = PropertyUtils.copyProperties(genTable, SysGenTableResp.class);
		resp.setColumns(PropertyUtils.copyArrayProperties(genTableService.listColumn(id),
				com.company.system.api.response.SysGenTableColumnResp.class));
		return resp;
	}

	/**
	 * 保存生成配置
	 */
	@Override
	public Boolean update(SysGenTableReq req) {
		return genTableService.update(req);
	}

	/**
	 * 导入表
	 */
	@Override
	public Boolean importTable(String tables) {
		return genTableService.importTable(tables);
	}

	/**
	 * 删除生成配置
	 */
	@Override
	public Boolean remove(RemoveReq<Integer> req) {
		return genTableService.remove(req.getIdList());
	}

	/**
	 * 同步数据库表结构
	 */
	@Override
	public Boolean synchDb(String tableName) {
		return genTableService.synchDb(tableName);
	}

	/**
	 * 执行建表语句
	 */
	@Override
	public Boolean createTable(String sql) {
		return genTableService.createTable(sql);
	}

	/**
	 * 预览生成的代码
	 */
	@Override
	public Map<String, String> preview(Integer id) {
		return genTemplateService.preview(id);
	}

	/**
	 * 生成代码并打包为 zip（base64）
	 */
	@Override
	public String downloadBase64(String tables) {
		byte[] zipBytes = genTemplateService.zip(tables);
		return Base64.getEncoder().encodeToString(zipBytes);
	}

	/**
	 * 生成代码到自定义路径
	 */
	@Override
	public Boolean genCode(String tableName) {
		GenTable genTable = genTableService.getByTableName(tableName);
		if (genTable == null) {
			ExceptionUtil.throwException("表[" + tableName + "]尚未导入生成配置");
		}
		// 未配置自定义路径（或配置为若依约定的 "/"）时生成到当前项目根目录
		String genPath = genTable.getGenPath();
		String projectPath = StringUtils.isBlank(genPath) || "/".equals(genPath.trim()) ? DEFAULT_GEN_PATH
				: genPath.trim();
		codeGeneratorService.generateCode(tableName, genTable.getModuleName(), projectPath);
		return true;
	}

}
