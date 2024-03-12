package com.company.user.mapper.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.MemberBuyOrder;

public interface MemberBuyOrderMapper extends BaseMapper<MemberBuyOrder> {

	@Select("select * from bu_member_buy_order where order_code = #{orderCode}")
	MemberBuyOrder selectByOrderCode(@Param("orderCode") String orderCode);
}
