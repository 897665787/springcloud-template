package com.company.system.api.interfaces;

/**
 * 通知公告服务接口
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
import com.company.system.api.request.SysNoticeReq;
import com.company.system.api.response.SysNoticeResp;

/**
 * 通知公告服务接口
 */
public interface SysNoticeApi {

	/**
	 * 分页查询公告
	 */
	@GetMapping("/page")
	PageResp<SysNoticeResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
			@RequestParam(value = "noticeTitle", required = false) String noticeTitle,
			@RequestParam(value = "noticeType", required = false) String noticeType,
			@RequestParam(value = "createBy", required = false) String createBy,
			@RequestParam(value = "status", required = false) String status);

	/**
	 * 查询公告详情
	 */
	@GetMapping("/query")
	SysNoticeResp query(@RequestParam("id") Integer id);

	/**
	 * 新增公告
	 */
	@PostMapping("/save")
	Boolean save(@RequestBody SysNoticeReq sysNoticeReq);

	/**
	 * 修改公告
	 */
	@PostMapping("/update")
	Boolean update(@RequestBody SysNoticeReq sysNoticeReq);

	/**
	 * 删除公告
	 */
	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);
}
