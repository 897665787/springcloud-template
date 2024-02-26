package com.company.user.mapper.market;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.UserCoupon;

public interface UserCouponMapper extends BaseMapper<UserCoupon> {

	@Select("select * from mk_user_coupon where user_id = #{userId} and status = #{status} order by id desc")
	List<UserCoupon> selectByUserIdStatus(@Param("userId") Integer userId, @Param("status") String status);

	@Update("update mk_user_coupon set status = #{newStatus} where id = #{id} and status = #{oldStatus}")
	Integer updateStatus(@Param("id") Integer id, @Param("oldStatus") String oldStatus,
			@Param("newStatus") String newStatus);
}
