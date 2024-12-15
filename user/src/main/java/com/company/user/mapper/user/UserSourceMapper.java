package com.company.user.mapper.user;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.UserSource;

public interface UserSourceMapper extends BaseMapper<UserSource> {

	@Insert("insert ignore into bu_user_source(deviceid,source,time) values (#{deviceid},#{source},#{time})")
	int saveOrIgnore(@Param("deviceid") String deviceid, @Param("source") String source,
			@Param("time") LocalDateTime time);

	@Select("select * from bu_user_source where deviceid = #{deviceid} order by id desc limit 1")
	UserSource selectLastByDeviceid(@Param("deviceid") String deviceid);
}
