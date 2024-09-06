package com.company.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

	@Select("select * from sys_user where account = #{account} and del_flag = '0'")
	SysUser selectByAccount(@Param("account") String account);

	boolean logicRemoveBatchByIds(@Param("idList") List<Integer> idList);

	List<SysUser> selectByIds(@Param("ids") Collection<Integer> ids);
}