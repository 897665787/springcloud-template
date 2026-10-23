package com.company.system.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Sets;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.constant.Constants;
import com.company.system.api.interfaces.SysUserRoleApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysUserRoleBatchReq;
import com.company.system.api.request.SysUserRoleReq;
import com.company.system.api.response.SysUserRoleResp;
import com.company.system.entity.SysRole;
import com.company.system.entity.SysUserRole;
import com.company.system.service.SysMenuService;
import com.company.system.service.SysRoleMenuService;
import com.company.system.service.SysRoleService;
import com.company.system.service.SysUserRoleService;

@RestController
@RequestMapping("/sysUserRole")
public class SysUserRoleController implements SysUserRoleApi {

	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysMenuService sysMenuService;

	private QueryWrapper<SysUserRole> toQueryWrapper(Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
		if (userId != null) {
			queryWrapper.eq("user_id", userId);
		}
		if (roleId != null) {
			queryWrapper.eq("role_id", roleId);
		}
        if (StringUtils.isNotBlank(createTimeStart)) {
			queryWrapper.ge("create_time", createTimeStart + " 00:00:00");
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            queryWrapper.le("create_time", createTimeEnd + " 23:59:59");
        }
        if (StringUtils.isNotBlank(updateTimeStart)) {
            queryWrapper.ge("update_time", updateTimeStart + " 00:00:00");
        }
        if (StringUtils.isNotBlank(updateTimeEnd)) {
            queryWrapper.le("update_time", updateTimeEnd + " 23:59:59");
        }
		return queryWrapper;
	}

	@Override
	public PageResp<SysUserRoleResp> page(Long current, Long size, Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUserRole> queryWrapper = toQueryWrapper(userId, roleId, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);

		long count = sysUserRoleService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysUserRole> list = sysUserRoleService.list(PageDTO.of(current, size), queryWrapper);

		List<SysUserRoleResp> respList = PropertyUtils.copyArrayProperties(list, SysUserRoleResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public List<SysUserRoleResp> list(Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUserRole> queryWrapper = toQueryWrapper(userId, roleId, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);

		queryWrapper.orderByDesc("id");
		List<SysUserRole> list = sysUserRoleService.list(queryWrapper);

		List<SysUserRoleResp> respList = PropertyUtils.copyArrayProperties(list, SysUserRoleResp.class);
		return respList;
	}

	@Override
	public SysUserRoleResp query(Integer id) {
		SysUserRole sysUserRole = sysUserRoleService.getById(id);
		SysUserRoleResp sysUserRoleResp = PropertyUtils.copyProperties(sysUserRole, SysUserRoleResp.class);
		return sysUserRoleResp;
	}

	@Override
	public Set<Integer> listRoleIdByUserId(Integer userId) {
		Set<Integer> roleIds = sysUserRoleService.listRoleIdByUserId(userId);
		return roleIds;
	}

	@Override
	public Boolean save(SysUserRoleReq sysUserRoleReq) {
		SysUserRole sysUserRole = PropertyUtils.copyProperties(sysUserRoleReq, SysUserRole.class);
		boolean success = sysUserRoleService.save(sysUserRole);
		return success;
	}

	@Override
	public Boolean update(SysUserRoleReq sysUserRoleReq) {
		SysUserRole sysUserRole = PropertyUtils.copyProperties(sysUserRoleReq, SysUserRole.class);
		boolean success = sysUserRoleService.updateById(sysUserRole);
		return success;
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return true;
		}
		boolean success = sysUserRoleService.removeBatchByIds(req.getIdList());
		return success;
	}

	@Override
	public Boolean hasPermission(Integer userId, String permission) {
		Set<Integer> roleIdSet = sysUserRoleService.listRoleIdByUserId(userId);
		if (CollectionUtils.isEmpty(roleIdSet)) {
			return false;
		}

		List<SysRole> sysRoleList = sysRoleService.listByIds(roleIdSet);
		if (CollectionUtils.isEmpty(sysRoleList)) {
			return false;
		}

		// 有1个角色是超管角色，则返回有权限
		for (SysRole sysRole : sysRoleList) {
			if (Constants.SUPER_ROLE.equalsIgnoreCase(sysRole.getRoleKey())) {
				return true;
			}
		}

		Set<Integer> menuIdSet = sysRoleMenuService.listMenuIdByRoleIds(roleIdSet);
		if (CollectionUtils.isEmpty(menuIdSet)) {
			return false;
		}

		Set<String> permsSet = sysMenuService.listPermsByMenuIds(menuIdSet);
		if (CollectionUtils.isEmpty(permsSet)) {
			return false;
		}
		return permsSet.contains(permission);
	}

	@Override
	public Boolean assignRoleBatch(@RequestBody SysUserRoleBatchReq req) {
		Set<Integer> roleIds = resolveRoleIds(req);
		if (req.getUserIds() == null || req.getUserIds().isEmpty() || roleIds.isEmpty()) {
			return true;
		}
		req.getUserIds().forEach(userId -> sysUserRoleService.assignRole(userId, roleIds));
		return true;
	}

	@Override
	public Boolean revokeBatch(@RequestBody SysUserRoleBatchReq req) {
		Set<Integer> roleIds = resolveRoleIds(req);
		if (req.getUserIds() == null || req.getUserIds().isEmpty() || roleIds.isEmpty()) {
			return true;
		}
		req.getUserIds().forEach(userId -> sysUserRoleService.revokeRole(userId, roleIds));
		return true;
	}

	/**
	 * 兼容单个 roleId 与 roleIds 两种入参
	 */
	private Set<Integer> resolveRoleIds(SysUserRoleBatchReq req) {
		if (req.getRoleIds() != null && !req.getRoleIds().isEmpty()) {
			return req.getRoleIds();
		}
		if (req.getRoleId() != null) {
			return Sets.newHashSet(req.getRoleId());
		}
		return Collections.emptySet();
	}
}
