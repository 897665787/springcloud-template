package com.company.tool.subscribe.type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.tool.subscribe.SubscribeType;
import com.company.tool.subscribe.SubscribeTypeBeanFactory;
import com.company.tool.subscribe.dto.SubscribeSendDto;
import com.google.common.collect.Lists;

@Component(SubscribeTypeBeanFactory.NEW_USER_COUPON_PKG_COUPON_REVICE)
public class NewUseCouponPkgCouponReviceSubscribeType implements SubscribeType {

	@Override
	public SubscribeSendDto wrapParam(Integer userId, Map<String, String> runtimeAttach) {
		String page = "index?foo=bar";
		List<String> valueList = Lists.newArrayList();
		LocalDateTime planSendTime = LocalDateTime.now();

		SubscribeSendDto subscribeSendDto = new SubscribeSendDto();
		subscribeSendDto.setPage(page);
		subscribeSendDto.setValueList(valueList);
		subscribeSendDto.setPlanSendTime(planSendTime);
		subscribeSendDto.setOverTime(subscribeSendDto.getPlanSendTime().plusHours(1));

		return subscribeSendDto;
	}
}
