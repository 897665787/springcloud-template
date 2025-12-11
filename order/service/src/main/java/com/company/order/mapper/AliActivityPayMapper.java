package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliActivityPay;

public interface AliActivityPayMapper extends BaseMapper<AliActivityPay> {

	@Select("select * from ali_activity_pay where out_order_no = #{outOrderNo}")
	AliActivityPay selectByOutOrderNo(@Param("outOrderNo") String outOrderNo);

	@Update("update ali_activity_pay set notify_data = #{notifyData},notify_status = 1 where out_order_no = #{outOrderNo} and notify_status != 4")
	int updateByOutOrderNo(@Param("outOrderNo") String outOrderNo, @Param("notifyData") String notifyData);

	@Update("update ali_activity_pay set notify_status = #{notifyStatus} where out_order_no = #{outOrderNo} and notify_status = #{oldNotifyStatus}")
	int updateNotifyStatusByOutOrderNo(@Param("outOrderNo") String outOrderNo,
			@Param("notifyStatus") Integer notifyStatus, @Param("oldNotifyStatus") Integer oldNotifyStatus);
}