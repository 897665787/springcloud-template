package com.company.user.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.DeviceInfo;
import com.company.user.entity.UserSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

	@Insert("insert ignore into device_info(deviceid,platform,operator,channel,version,requestip,request_user_agent,time)"
			+ " values (#{deviceid},#{platform},#{operator},#{channel},#{version},#{requestip},#{requestUserAgent},#{time})"
			+ " ON DUPLICATE KEY UPDATE"
			+ " platform = #{platform},operator = #{operator},channel = #{channel},version = #{version},requestip = #{requestip},request_user_agent = #{requestUserAgent},time = #{time}")
	int saveOrIgnore(@Param("deviceid") String deviceid, @Param("platform") String platform, @Param("operator") String operator, @Param("channel") String channel, @Param("version") String version, @Param("requestip") String requestip, @Param("requestUserAgent") String requestUserAgent, @Param("time") LocalDateTime time);

	@Select("select * from device_info where deviceid = #{deviceid}")
	DeviceInfo selectByDeviceid(@Param("deviceid") String deviceid);
}
