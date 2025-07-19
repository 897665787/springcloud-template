package com.company.user.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

//	@DS("slave_1") // 查询从库1
//	@DS("slave_2") // 查询从库2
//	@DS("slave") // 查询分组slave的数据
	@Slave // 查询分组slave的数据
	@Select("select * from bu_user_info where id = #{id}")
	UserInfo getById(@Param("id") Integer id);
}
