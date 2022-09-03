package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.WxPay;

public interface WxPayMapper extends BaseMapper<WxPay> {
	
	@Select("select * from bu_wx_pay where out_trade_no = #{outTradeNo}")
	WxPay selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);
}