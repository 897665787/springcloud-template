package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.FinancialFlow;

public interface FinancialFlowMapper extends BaseMapper<FinancialFlow> {

	@Select("select * from bu_financial_flow where order_code = #{orderCode}")
	FinancialFlow selectByOrderCode(@Param("orderCode") String orderCode);
}
