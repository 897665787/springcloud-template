package com.company.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysUserRole;
import com.company.system.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements IService<SysUserRole> {

	public Set<Integer> listRoleIdByUserId(Integer userId) {
		return baseMapper.listRoleIdByUserId(userId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void assignRole(Integer sysUserId, Set<Integer> roleIds) {
		Set<Integer> oldRoleIdSet = this.listRoleIdByUserId(sysUserId);
		Set<Integer> roleIdsToDel = oldRoleIdSet.stream().filter(v -> !roleIds.contains(v)).collect(Collectors.toSet());
		if (!roleIdsToDel.isEmpty()) {
			baseMapper.revoke(sysUserId, roleIdsToDel);
		}
		Set<Integer> roleIdsToInsert = roleIds.stream().filter(v -> !oldRoleIdSet.contains(v)).collect(Collectors.toSet());
		if (!roleIdsToInsert.isEmpty()) {
			baseMapper.grant(sysUserId, roleIdsToInsert);
		}
	}

}