package com.company.system.api.interfaces;

/**
 * 定时任务日志服务接口
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
import com.company.system.api.response.SysJobLogResp;

/**
 * 定时任务日志服务接口
 */
public interface SysJobLogApi {

	/**
	 * 分页查询任务日志
	 */
	@GetMapping("/page")
	PageResp<SysJobLogResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "jobName", required = false) String jobName,
			@RequestParam(value = "jobGroup", required = false) String jobGroup,
			@RequestParam(value = "status", required = false) String status);

	/**
	 * 删除任务日志
	 */
	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	/**
	 * 清空任务日志
	 */
	@PostMapping("/clean")
	Boolean clean();
}
