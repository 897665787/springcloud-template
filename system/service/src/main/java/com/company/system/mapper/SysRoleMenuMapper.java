package com.company.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

	Set<Integer> listMenuIdByRoleIds(@Param("roleIds") Set<Integer> roleIds);

	/**
	 * 注意：menuIds不能为空
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	Integer grant(@Param("roleId") Integer roleId, @Param("menuIds") Set<Integer> menuIds);

	Integer revoke(@Param("roleId") Integer roleId, @Param("menuIds") Set<Integer> menuIds);
}