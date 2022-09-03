package com.company.order.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.OrderPayRefund;

public interface OrderPayRefundMapper extends BaseMapper<OrderPayRefund> {

	@Select("select * from bu_order_pay_refund where refund_order_code = #{refundOrderCode}")
	OrderPayRefund selectByRefundOrderCode(@Param("refundOrderCode") String refundOrderCode);

	@Select("select sum(amount) from bu_order_pay_refund where order_code = #{orderCode} and status in('apply','success')")
	BigDecimal sumRefundAmount(@Param("orderCode") String orderCode);

	@Select("select * from bu_order_pay_refund where order_code = #{orderCode}")
	List<OrderPayRefund> listByOrderCode(@Param("orderCode") String orderCode);
}