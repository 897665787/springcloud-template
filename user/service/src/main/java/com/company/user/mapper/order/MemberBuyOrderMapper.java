package com.company.user.mapper.order;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.MemberBuyOrder;

public interface MemberBuyOrderMapper extends BaseMapper<MemberBuyOrder> {

	@Select("select * from member_buy_order where order_code = #{orderCode}")
	MemberBuyOrder selectByOrderCode(@Param("orderCode") String orderCode);

	@Update("update member_buy_order set refund_service_amount = #{refundServiceAmount} where order_code = #{orderCode}")
	Integer updateRefundServiceAmountByOrderCode(@Param("refundServiceAmount") BigDecimal refundServiceAmount,
			@Param("orderCode") String orderCode);
}
