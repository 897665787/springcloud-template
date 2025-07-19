package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliActivityPayRefund;

public interface AliActivityPayRefundMapper extends BaseMapper<AliActivityPayRefund> {

	@Select("select * from bu_ali_activity_pay_refund where out_biz_no = #{outBizNo}")
	AliActivityPayRefund selectByOutBizNo(@Param("outBizNo") String outBizNo);

	/**
	 * 对out_order_no添加唯一索引，每笔订单只能有1次退款
	 * 
	 * @param outOrderNo
	 * @return
	 */
	@Select("select * from bu_ali_activity_pay_refund where out_order_no = #{outOrderNo}")
	AliActivityPayRefund selectByOutOrderNo(@Param("outOrderNo") String outOrderNo);
}