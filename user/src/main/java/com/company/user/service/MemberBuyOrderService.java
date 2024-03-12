package com.company.user.service;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.MemberBuyOrder;
import com.company.user.mapper.order.MemberBuyOrderMapper;

@Component
public class MemberBuyOrderService extends ServiceImpl<MemberBuyOrderMapper, MemberBuyOrder>
		implements IService<MemberBuyOrder> {

	public MemberBuyOrder selectByOrderCode(String orderCode) {
		return baseMapper.selectByOrderCode(orderCode);
	}
}
