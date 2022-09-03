package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.WxPayRefund;

public interface WxPayRefundMapper extends BaseMapper<WxPayRefund> {
	
	@Select("select * from bu_wx_pay_refund where out_refund_no = #{outRefundNo}")
	WxPayRefund selectByOutRefundNo(@Param("outRefundNo") String outRefundNo);
}