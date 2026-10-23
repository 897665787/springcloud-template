package com.company.system.api.interfaces;


import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysUserRoleBatchReq;
import com.company.system.api.request.SysUserRoleReq;
import com.company.common.response.PageResp;
import com.company.system.api.response.SysUserRoleResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface SysUserRoleApi {

	@GetMapping("/page")
	PageResp<SysUserRoleResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "roleId", required = false) Integer roleId, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/list")
	List<SysUserRoleResp> list(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "roleId", required = false) Integer roleId, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	SysUserRoleResp query(@RequestParam("id") Integer id);

	@GetMapping("/listRoleIdByUserId")
	Set<Integer> listRoleIdByUserId(@RequestParam("userId") Integer userId);

	@PostMapping("/save")
	Boolean save(@RequestBody SysUserRoleReq sysUserRoleReq);

	@PostMapping("/update")
	Boolean update(@RequestBody SysUserRoleReq sysUserRoleReq);

	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	/**
	 * 批量授予角色（用于角色侧「添加用户」）
	 */
	@PostMapping("/assignRoleBatch")
	Boolean assignRoleBatch(@RequestBody SysUserRoleBatchReq req);

	/**
	 * 批量取消角色授权（用于角色侧「取消授权」）
	 */
	@PostMapping("/revokeBatch")
	Boolean revokeBatch(@RequestBody SysUserRoleBatchReq req);

	@GetMapping("/hasPermission")
	Boolean hasPermission(@RequestParam("userId") Integer userId,
			@RequestParam("permission") String permission);
}