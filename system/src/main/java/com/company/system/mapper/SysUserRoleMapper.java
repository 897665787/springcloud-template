package com.company.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	@Select("select role_id from sys_user_role as ur join sys_role as r on r.id = ur.role_id where ur.user_id = #{userId} and r.status = 'ON'")
	Set<Integer> listRoleIdByUserId(@Param("userId") Integer userId);

	@Select("select count(*) from sys_user_role as ur join sys_role as r on r.id = ur.role_id where ur.user_id = #{userId} and r.role_key = #{roleKey} and r.status = 'ON'")
	int countByUserIdRoleKey(@Param("userId") Integer userId, @Param("roleKey") String roleKey);

	Integer grant(@Param("sysUserId") Integer sysUserId, @Param("roleIds") Set<Integer> roleIds);

	Integer revoke(@Param("sysUserId") Integer sysUserId, @Param("roleIds") Set<Integer> roleIds);

}