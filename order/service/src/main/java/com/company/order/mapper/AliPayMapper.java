package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliPay;

public interface AliPayMapper extends BaseMapper<AliPay> {

	@Select("select * from ali_pay where out_trade_no = #{outTradeNo}")
	AliPay selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);

	@Update("update ali_pay set notify_data = #{notifyData},notify_status = 1 where out_trade_no = #{outTradeNo} and notify_status != 4")
	int updateByOutTradeNo(@Param("outTradeNo") String outTradeNo, @Param("notifyData") String notifyData);

	@Update("update ali_pay set notify_status = #{notifyStatus} where out_trade_no = #{outTradeNo} and notify_status = #{oldNotifyStatus}")
	int updateNotifyStatusByOutTradeNo(@Param("outTradeNo") String outTradeNo,
			@Param("notifyStatus") Integer notifyStatus, @Param("oldNotifyStatus") Integer oldNotifyStatus);
}