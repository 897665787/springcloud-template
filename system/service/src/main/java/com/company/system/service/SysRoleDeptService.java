package com.company.system.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysRoleDept;
import com.company.system.mapper.SysRoleDeptMapper;

@Service
public class SysRoleDeptService extends ServiceImpl<SysRoleDeptMapper, SysRoleDept>
		implements IService<SysRoleDept> {

	/**
	 * 查询角色已授权的部门id
	 */
	public Set<Integer> listDeptIdByRoleId(Integer roleId) {
		LambdaQueryWrapper<SysRoleDept> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SysRoleDept::getRoleId, roleId);
		return this.list(wrapper).stream().map(SysRoleDept::getDeptId).collect(Collectors.toSet());
	}

	/**
	 * 角色数据权限授权（全量覆盖）
	 */
	@Transactional(rollbackFor = Exception.class)
	public void grantDept(Integer roleId, Set<Integer> deptIds) {
		LambdaQueryWrapper<SysRoleDept> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SysRoleDept::getRoleId, roleId);
		this.remove(wrapper);

		if (deptIds == null || deptIds.isEmpty()) {
			return;
		}
		deptIds.forEach(deptId -> {
			SysRoleDept sysRoleDept = new SysRoleDept();
			sysRoleDept.setRoleId(roleId);
			sysRoleDept.setDeptId(deptId);
			this.save(sysRoleDept);
		});
	}
}
