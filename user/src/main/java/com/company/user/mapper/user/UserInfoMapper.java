package com.company.user.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.database.datasource.annotation.DataSource;
import com.company.user.entity.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

//	@DataSource(Slave.SLAVE1)
//	@DataSource(Slave.SLAVE_RANDOM)
	@DataSource
	@Select("select * from bu_user_info where id = #{id}")
	UserInfo getById(@Param("id") Integer id);
}
