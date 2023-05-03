package com.company.user.mapper.market;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.UserCoupon;

public interface UserCouponMapper extends BaseMapper<UserCoupon> {

	@Select("select * from mk_user_coupon where user_id = #{userId} and status = #{status} order by id desc")
	List<UserCoupon> selectByUserIdStatus(@Param("userId") Integer userId, @Param("status") String status);
}
