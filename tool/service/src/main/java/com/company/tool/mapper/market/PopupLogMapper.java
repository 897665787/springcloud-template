package com.company.tool.mapper.market;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.PopupLog;

public interface PopupLogMapper extends BaseMapper<PopupLog> {

	@Select("select create_time from popup_log where user_id = #{userId} order by id desc limit 1")
	LocalDateTime lastByUserId(@Param("userId") Integer userId);

	@Select("select create_time from popup_log where deviceid = #{deviceid} order by id desc limit 1")
	LocalDateTime lastByDeviceid(@Param("deviceid") String deviceid);

	@Select("select create_time from popup_log"
			+ " where business_type = #{businessType.code} and business_id = #{businessId}"
			+ " and (user_id = #{userId} or deviceid = #{deviceid})"
			+ " order by id desc limit 1")
	LocalDateTime lastByBusiness(@Param("businessType") PopupEnum.LogBusinessType businessType,
			@Param("businessId") Integer businessId, @Param("userId") Integer userId,
			@Param("deviceid") String deviceid);

	@Select("select count(*) from popup_log"
			+ " where business_type = #{businessType.code} and business_id = #{businessId}"
			+ " and (user_id = #{userId} or deviceid = #{deviceid})"
			+ " and (create_time >= #{beginTime} and create_time < #{endTime})")
	Integer countByBusiness(@Param("businessType") PopupEnum.LogBusinessType businessType,
			@Param("businessId") Integer businessId, @Param("userId") Integer userId,
			@Param("deviceid") String deviceid, @Param("beginTime") LocalDateTime beginTime,
			@Param("endTime") LocalDateTime endTime);
}
