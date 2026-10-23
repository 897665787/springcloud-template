package com.company.system.api.interfaces;

/**
 * 定时任务服务接口
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.response.PageResp;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysJobReq;
import com.company.system.api.response.SysJobResp;

/**
 * 定时任务服务接口
 */
public interface SysJobApi {

	/**
	 * 分页查询任务
	 */
	@GetMapping("/page")
	PageResp<SysJobResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "jobName", required = false) String jobName,
			@RequestParam(value = "jobGroup", required = false) String jobGroup,
			@RequestParam(value = "status", required = false) String status);

	/**
	 * 查询任务详情
	 */
	@GetMapping("/query")
	SysJobResp query(@RequestParam("id") Integer id);

	/**
	 * 新增任务
	 */
	@PostMapping("/save")
	Boolean save(@RequestBody SysJobReq sysJobReq);

	/**
	 * 修改任务
	 */
	@PostMapping("/update")
	Boolean update(@RequestBody SysJobReq sysJobReq);

	/**
	 * 删除任务
	 */
	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	/**
	 * 修改任务状态(0:正常 1:暂停)
	 */
	@PostMapping("/changeStatus")
	Boolean changeStatus(@RequestBody SysJobReq sysJobReq);

	/**
	 * 立即执行一次
	 */
	@PostMapping("/run")
	Boolean run(@RequestBody SysJobReq sysJobReq);
}
