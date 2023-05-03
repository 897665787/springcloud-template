package com.company.user.coupon.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.user.coupon.UseCondition;
import com.company.user.coupon.UseParam;
import com.company.user.coupon.dto.MatchResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 首单可用
 */
@Slf4j
@Component("FirstOrderCondition")
public class FirstOrderCondition implements UseCondition {

	// 模拟数据库存储的订单数据，key是用户ID，value是订单列表
	Map<Integer, List<Integer>> mockOrderListMap = Maps.newHashMap();
	{
		mockOrderListMap.put(2, Lists.newArrayList(1, 2, 3, 4));
		mockOrderListMap.put(3, Lists.newArrayList(11, 12, 14));
	}

	@Override
	public MatchResult canUse(UseParam useParam) {
		Integer userId = useParam.getUserId();

		// 根据用户查询有无历史订单
		List<Integer> mockOrderList = mockOrderListMap.getOrDefault(userId, Lists.newArrayList());
		boolean canUse = mockOrderList.isEmpty();
		if (!canUse) {
			log.info("{}校验条件不成立:{} {}", useParam.getUserCouponId(), userId, mockOrderList.size());
			return MatchResult.builder().canUse(false).reason("仅首单可用").build();
		}
		return MatchResult.builder().canUse(canUse).build();
	}
}