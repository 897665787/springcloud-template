package com.company.system.api.interfaces;

/**
 * 岗位服务接口
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.response.PageResp;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysPostReq;
import com.company.system.api.response.SysPostResp;

/**
 * 岗位服务接口
 */
public interface SysPostApi {

	/**
	 * 分页查询岗位
	 */
	@GetMapping("/page")
	PageResp<SysPostResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "postCode", required = false) String postCode,
			@RequestParam(value = "postName", required = false) String postName,
			@RequestParam(value = "status", required = false) String status);

	/**
	 * 查询岗位列表
	 */
	@GetMapping("/list")
	List<SysPostResp> list(@RequestParam(value = "postCode", required = false) String postCode,
			@RequestParam(value = "postName", required = false) String postName,
			@RequestParam(value = "status", required = false) String status);

	/**
	 * 查询岗位详情
	 */
	@GetMapping("/query")
	SysPostResp query(@RequestParam("id") Integer id);

	/**
	 * 新增岗位
	 */
	@PostMapping("/save")
	Boolean save(@RequestBody SysPostReq sysPostReq);

	/**
	 * 修改岗位
	 */
	@PostMapping("/update")
	Boolean update(@RequestBody SysPostReq sysPostReq);

	/**
	 * 删除岗位
	 */
	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);
}
