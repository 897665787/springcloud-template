package com.company.adminapi.controller;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.excel.SysRoleExcel;

import com.company.system.api.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.adminapi.feign.client.SysRoleFeign;
import com.company.adminapi.feign.client.SysUserFeign;
import com.company.system.api.request.SysRoleGrantDeptReq;
import com.company.system.api.request.SysRoleGrantMenuReq;
import com.company.system.api.request.SysRoleReq;
import com.company.system.api.response.SysRoleResp;
import com.company.system.api.response.SysUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import com.company.system.api.request.SysRoleReq;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

	@Autowired
	private SysRoleFeign sysRoleFeign;
	@Autowired
	private SysUserFeign sysUserFeign;

	@RequirePermissions("system:role:list")
	@GetMapping("/page")
	public PageResp<SysRoleResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, String roleName, String roleKey, Integer roleSort, String dataScope, String status, String roleRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		return sysRoleFeign.page(current, size, roleName, roleKey, roleSort, dataScope, status, roleRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
	}

	@RequirePermissions("system:role:list")
	@GetMapping("/list")
	public List<SysRoleResp> list(String roleName, String roleKey, Integer roleSort, String dataScope, String status, String roleRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		return sysRoleFeign.list(roleName, roleKey, roleSort, dataScope, status, roleRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
	}

	@RequirePermissions("system:role:list")
	@GetMapping("/query")
	public SysRoleResp query(@NotNull Integer id) {
		return sysRoleFeign.query(id);
	}

    @OperationLog(title = "角色信息保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:role:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysRoleReq sysRoleReq) {
		return sysRoleFeign.save(sysRoleReq);
	}

    @OperationLog(title = "角色信息更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:role:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysRoleReq sysRoleReq) {
		return sysRoleFeign.update(sysRoleReq);
	}

    @OperationLog(title = "角色信息删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:role:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysRoleFeign.remove(req);
	}

	@OperationLog(title = "角色信息导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:role:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String roleName, String roleKey, Integer roleSort, String dataScope, String status, String roleRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		List<SysRoleResp> listResp = sysRoleFeign.list(roleName, roleKey, roleSort, dataScope, status, roleRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
		List<SysRoleExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysRoleExcel.class);
		ExcelUtil.write2httpResponse(response, "角色信息", SysRoleExcel.class, excelList);
	}

	@OperationLog(title = "角色授权菜单", businessType = BusinessType.GRANT)
	@RequirePermissions("system:role:edit")
	@PostMapping("/grantMenu")
	public Boolean grantMenu(@RequestBody SysRoleGrantMenuReq req) {
		return sysRoleFeign.grantMenu(req);
	}


	/**
	 * 新增角色并授权菜单
	 */
	@OperationLog(title = "角色信息保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:role:add")
	@PostMapping("/saveAndGrant")
	public Boolean saveAndGrant(@RequestBody SysRoleReq sysRoleReq) {
		return sysRoleFeign.saveAndGrant(sysRoleReq);
	}

	/**
	 * 修改角色并授权菜单
	 */
	@OperationLog(title = "角色信息更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:role:edit")
	@PostMapping("/updateAndGrant")
	public Boolean updateAndGrant(@RequestBody SysRoleReq sysRoleReq) {
		return sysRoleFeign.updateAndGrant(sysRoleReq);
	}

	/**
	 * 查询角色已授权的用户列表
	 */
	@RequirePermissions("system:role:list")
	@GetMapping("/allocatedUserList")
	public PageResp<com.company.adminapi.resp.SysUserResp> allocatedUserList(@NotNull @Min(value = 1) Long current, @NotNull Long size, @NotNull Integer roleId, String account, String nickname, String phonenumber) {
		PageResp<SysUserResp> pageResp = sysUserFeign.pageAllocatedByRole(current, size, roleId, account, nickname, phonenumber);
		List<com.company.adminapi.resp.SysUserResp> respList = PropertyUtils.copyArrayProperties(pageResp.getList(), com.company.adminapi.resp.SysUserResp.class);
		return PageResp.of(pageResp.getTotal(), respList);
	}

	/**
	 * 查询角色未授权的用户列表
	 */
	@RequirePermissions("system:role:list")
	@GetMapping("/unallocatedUserList")
	public PageResp<com.company.adminapi.resp.SysUserResp> unallocatedUserList(@NotNull @Min(value = 1) Long current, @NotNull Long size, @NotNull Integer roleId, String account, String nickname, String phonenumber) {
		PageResp<SysUserResp> pageResp = sysUserFeign.pageUnallocatedByRole(current, size, roleId, account, nickname, phonenumber);
		List<com.company.adminapi.resp.SysUserResp> respList = PropertyUtils.copyArrayProperties(pageResp.getList(), com.company.adminapi.resp.SysUserResp.class);
		return PageResp.of(pageResp.getTotal(), respList);
	}

	/**
	 * 查询角色已授权的部门id列表
	 */
	@RequirePermissions("system:role:list")
	@GetMapping("/listDeptIdByRoleId")
	public Set<Integer> listDeptIdByRoleId(@NotNull Integer roleId) {
		return sysRoleFeign.listDeptIdByRoleId(roleId);
	}

	/**
	 * 角色数据权限授权
	 */
	@OperationLog(title = "角色数据权限", businessType = BusinessType.GRANT)
	@RequirePermissions("system:role:edit")
	@PostMapping("/grantDept")
	public Boolean grantDept(@RequestBody SysRoleGrantDeptReq req) {
		return sysRoleFeign.grantDept(req);
	}
}
