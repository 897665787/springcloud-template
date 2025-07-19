package com.company.user.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.user.entity.MemberBuyOrder;
import com.company.user.mapper.order.MemberBuyOrderMapper;

@Component
public class MemberBuyOrderService extends ServiceImpl<MemberBuyOrderMapper, MemberBuyOrder>
		implements IService<MemberBuyOrder> {

	public MemberBuyOrder selectByOrderCode(String orderCode) {
		return baseMapper.selectByOrderCode(orderCode);
	}

	public Integer updateRefundServiceAmountByOrderCode(BigDecimal serviceAmount, String orderCode) {
		return baseMapper.updateRefundServiceAmountByOrderCode(serviceAmount, orderCode);
	}
}
