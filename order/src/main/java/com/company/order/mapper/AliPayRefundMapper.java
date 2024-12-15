package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliPayRefund;

public interface AliPayRefundMapper extends BaseMapper<AliPayRefund> {
	
	@Select("select * from bu_ali_pay_refund where out_request_no = #{outRequestNo}")
	AliPayRefund selectByOutRequestNo(@Param("outRequestNo") String outRequestNo);
	
}