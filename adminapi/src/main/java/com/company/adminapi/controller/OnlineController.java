package com.company.adminapi.controller;

/**
 * 在线用户监控
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.dto.OnlineSession;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.service.OnlineUserService;

/**
 * 在线用户监控
 */
@RestController
@RequestMapping("/monitor")
public class OnlineController {

	@Autowired
	private OnlineUserService onlineUserService;

	/**
	 * 查询在线用户列表
	 */
	@RequirePermissions("monitor:online:list")
	@GetMapping("/online/list")
	public Map<String, Object> list(String ipaddr, String userName) {
		List<OnlineSession> sessions = onlineUserService.list().stream()
				.filter(session -> StringUtils.isBlank(ipaddr) || StringUtils.contains(session.getIpaddr(), ipaddr))
				.filter(session -> StringUtils.isBlank(userName) || StringUtils.contains(session.getAccount(), userName))
				.collect(Collectors.toList());

		Map<String, Object> result = new HashMap<>();
		result.put("total", (long) sessions.size());
		result.put("rows", sessions.stream().map(this::toView).collect(Collectors.toList()));
		return result;
	}

	/**
	 * 强退用户
	 */
	@OperationLog(title = "在线用户强退", businessType = BusinessType.FORCE)
	@RequirePermissions("monitor:online:forceLogout")
	@DeleteMapping("/online/{tokenId}")
	public Boolean forceLogout(@PathVariable String tokenId) {
		onlineUserService.remove(tokenId);
		onlineUserService.blacklist(tokenId, null);
		return true;
	}

	/**
	 * 会话 -> 若依前端字段
	 */
	private Map<String, Object> toView(OnlineSession session) {
		Map<String, Object> view = new LinkedHashMap<>();
		view.put("tokenId", session.getTokenId());
		view.put("userName", session.getAccount());
		view.put("deptName", session.getDeptName());
		view.put("ipaddr", session.getIpaddr());
		view.put("loginLocation", "");
		view.put("browser", "");
		view.put("os", "");
		view.put("loginTime", session.getLoginTime());
		return view;
	}
}
