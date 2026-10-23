package com.company.system.api.interfaces;


import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysRoleGrantDeptReq;
import com.company.system.api.request.SysRoleGrantMenuReq;
import com.company.system.api.request.SysRoleReq;
import com.company.common.response.PageResp;
import com.company.system.api.response.SysRoleResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface SysRoleApi {

	@GetMapping("/page")
	PageResp<SysRoleResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "roleName", required = false) String roleName, @RequestParam(value = "roleKey", required = false) String roleKey, @RequestParam(value = "roleSort", required = false) Integer roleSort, @RequestParam(value = "dataScope", required = false) String dataScope, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "roleRemark", required = false) String roleRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/list")
	List<SysRoleResp> list(@RequestParam(value = "roleName", required = false) String roleName, @RequestParam(value = "roleKey", required = false) String roleKey, @RequestParam(value = "roleSort", required = false) Integer roleSort, @RequestParam(value = "dataScope", required = false) String dataScope, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "roleRemark", required = false) String roleRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	SysRoleResp query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Boolean save(@RequestBody SysRoleReq sysRoleReq);

	@PostMapping("/saveReturnId")
	Integer saveReturnId(@RequestBody SysRoleReq sysRoleReq);

	/**
	 * 新增角色并授权菜单
	 */
	@PostMapping("/saveAndGrant")
	Boolean saveAndGrant(@RequestBody SysRoleReq sysRoleReq);

	/**
	 * 修改角色并授权菜单
	 */
	@PostMapping("/updateAndGrant")
	Boolean updateAndGrant(@RequestBody SysRoleReq sysRoleReq);

	@PostMapping("/update")
	Boolean update(@RequestBody SysRoleReq sysRoleReq);

	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	@PostMapping("/grantMenu")
	Boolean grantMenu(@RequestBody SysRoleGrantMenuReq req);

	/**
	 * 角色数据权限授权
	 */
	@PostMapping("/grantDept")
	Boolean grantDept(@RequestBody SysRoleGrantDeptReq req);

	/**
	 * 查询角色已授权的部门id列表
	 */
	@GetMapping("/listDeptIdByRoleId")
	Set<Integer> listDeptIdByRoleId(@RequestParam("roleId") Integer roleId);

}