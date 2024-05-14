package com.company.user.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.RechargeOrder;
import com.company.user.mapper.order.RechargeOrderMapper;

@Component
public class RechargeOrderService extends ServiceImpl<RechargeOrderMapper, RechargeOrder>
		implements IService<RechargeOrder> {

	public RechargeOrder selectByOrderCode(String orderCode) {
		return baseMapper.selectByOrderCode(orderCode);
	}

	public Integer updateRefundServiceAmountByOrderCode(BigDecimal serviceAmount, String orderCode) {
		return baseMapper.updateRefundServiceAmountByOrderCode(serviceAmount, orderCode);
	}
}
