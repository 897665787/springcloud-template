package com.company.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliActivityCoupon;

public interface AliActivityCouponMapper extends BaseMapper<AliActivityCoupon> {

	@Select("select * from ali_activity_coupon where event_id = #{eventId}")
	AliActivityCoupon selectByEventId(@Param("eventId") String eventId);

	@Select("select * from ali_activity_coupon where activity_id = #{activityId} and receive_user_id = #{receiveUserId}")
	List<AliActivityCoupon> selectByActivityIdReceiveUserId(@Param("activityId") String activityId,
			@Param("receiveUserId") String receiveUserId);

}