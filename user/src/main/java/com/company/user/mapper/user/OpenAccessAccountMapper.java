package com.company.user.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.database.datasource.annotation.DataSource;
import com.company.user.entity.OpenAccessAccount;

public interface OpenAccessAccountMapper extends BaseMapper<OpenAccessAccount> {

	@DataSource
	@Select("select app_key from open_access_account where appid = #{appid}")
	String getAppKeyByAppid(@Param("appid") String appid);
}
