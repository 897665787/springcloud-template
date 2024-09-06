package com.company.system.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.company.system.entity.SysRoleMenu;
import com.company.system.mapper.SysRoleMenuMapper;

@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
		implements IService<SysRoleMenu> {

	public Set<Integer> listMenuIdByRoleIds(Set<Integer> roleIds) {
		return baseMapper.listMenuIdByRoleIds(roleIds);
	}

	@Transactional(rollbackFor = Exception.class)
	public void grantMenu(Integer roleId, Set<Integer> menuIds) {
		Set<Integer> oldMenuIds = this.listMenuIdByRoleIds(Sets.newHashSet(roleId));
		Set<Integer> menuIdsToDel = oldMenuIds.stream().filter(v -> !menuIds.contains(v)).collect(Collectors.toSet());
		this.revoke(roleId, menuIdsToDel);
		Set<Integer> menuIdsToAdd = menuIds.stream().filter(v -> !oldMenuIds.contains(v)).collect(Collectors.toSet());
		this.grant(roleId, menuIdsToAdd);
	}

	public void grant(Integer roleId, Set<Integer> menuIds) {
		if (menuIds == null || menuIds.isEmpty()) {
			return;
		}
		baseMapper.grant(roleId, menuIds);
	}

	public void revoke(Integer roleId, Set<Integer> menuIds) {
		baseMapper.revoke(roleId, menuIds);
	}

}