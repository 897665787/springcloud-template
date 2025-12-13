package com.company.user.mapper.order;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.RechargeOrder;

public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {

	@Select("select * from recharge_order where order_code = #{orderCode}")
	RechargeOrder selectByOrderCode(@Param("orderCode") String orderCode);

	@Update("update recharge_order set refund_service_amount = #{refundServiceAmount} where order_code = #{orderCode}")
	Integer updateRefundServiceAmountByOrderCode(@Param("refundServiceAmount") BigDecimal refundServiceAmount,
			@Param("orderCode") String orderCode);
}
