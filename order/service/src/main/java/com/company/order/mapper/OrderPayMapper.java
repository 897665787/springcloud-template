package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.OrderPay;

public interface OrderPayMapper extends BaseMapper<OrderPay> {

	@Select("select * from order_pay where order_code = #{orderCode}")
	OrderPay selectByOrderCode(@Param("orderCode") String orderCode);
}