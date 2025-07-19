package com.company.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysRole;
import com.company.system.entity.SysUser;
import com.company.system.mapper.SysRoleDeptMapper;
import com.company.system.mapper.SysRoleMapper;
import com.company.system.mapper.SysUserMapper;
import com.company.system.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole>
        implements IService<SysRole> {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;

    public Set<Integer> listRolesByUserId(Integer userId) {
        SysUser sysUser = this.sysUserMapper.selectById(userId);
        if (Objects.isNull(sysUser)) {
            return Collections.emptySet();
        }

        Set<Integer> userRoleIds = this.sysUserRoleMapper.listRoleIdByUserId(userId);
        Set<Integer> deptRoleIds = this.sysRoleDeptMapper.listRoleIdsByDeptId(sysUser.getDeptId());

        return Stream.concat(userRoleIds.stream(), deptRoleIds.stream()).collect(Collectors.toSet());
    }

    public boolean hasRole(Integer userId, String roleKey) {
        return sysUserRoleMapper.countByUserIdRoleKey(userId, roleKey) > 0;
    }

}