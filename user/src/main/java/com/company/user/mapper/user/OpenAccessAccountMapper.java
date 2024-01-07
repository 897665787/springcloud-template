package com.company.user.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.OpenAccessAccount;

public interface OpenAccessAccountMapper extends BaseMapper<OpenAccessAccount> {

	@DS("slave_1") // 查询从库1
	@Select("select app_key from open_access_account where appid = #{appid}")
	String getAppKeyByAppid(@Param("appid") String appid);
}
