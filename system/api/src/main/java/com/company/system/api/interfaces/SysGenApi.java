package com.company.system.api.interfaces;

/**
 * 代码生成服务接口
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.response.PageResp;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysGenTableReq;
import com.company.system.api.response.SysGenTableResp;

/**
 * 代码生成服务接口
 */
public interface SysGenApi {

	/**
	 * 分页查询已导入的生成配置
	 */
	@GetMapping("/page")
	PageResp<SysGenTableResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "tableName", required = false) String tableName,
			@RequestParam(value = "tableComment", required = false) String tableComment);

	/**
	 * 分页查询数据库中可导入的表
	 */
	@GetMapping("/dbPage")
	PageResp<Map<String, Object>> dbPage(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "tableName", required = false) String tableName,
			@RequestParam(value = "tableComment", required = false) String tableComment);

	/**
	 * 查询生成配置详情（含字段）
	 */
	@GetMapping("/query")
	SysGenTableResp query(@RequestParam("id") Integer id);

	/**
	 * 保存生成配置
	 */
	@PostMapping("/update")
	Boolean update(@RequestBody SysGenTableReq req);

	/**
	 * 导入表
	 */
	@PostMapping("/importTable")
	Boolean importTable(@RequestParam("tables") String tables);

	/**
	 * 删除生成配置
	 */
	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	/**
	 * 同步数据库表结构
	 */
	@PostMapping("/synchDb")
	Boolean synchDb(@RequestParam("tableName") String tableName);

	/**
	 * 执行建表语句
	 */
	@PostMapping("/createTable")
	Boolean createTable(@RequestParam("sql") String sql);

	/**
	 * 预览生成的代码，key 为模板文件名，value 为文件内容
	 */
	@GetMapping("/preview")
	Map<String, String> preview(@RequestParam("id") Integer id);

	/**
	 * 生成代码并打包为 zip，返回 base64 编码内容
	 */
	@GetMapping("/download")
	String downloadBase64(@RequestParam("tables") String tables);

	/**
	 * 生成代码到自定义路径
	 */
	@PostMapping("/genCode")
	Boolean genCode(@RequestParam("tableName") String tableName);
}
