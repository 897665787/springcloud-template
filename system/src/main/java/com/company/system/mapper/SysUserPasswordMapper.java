package com.company.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysUserPassword;

public interface SysUserPasswordMapper extends BaseMapper<SysUserPassword> {

	@Select("select * from sys_user_password where sys_user_id = #{sysUserId} order by id desc limit 1")
	SysUserPassword selectLastBySysUserId(@Param("sysUserId") Integer sysUserId);

	@Update("update sys_user_password set expire_login_times = expire_login_times + 1 where id = #{id}")
	Integer incrExpireLoginTimesById(@Param("id") Integer id);

}