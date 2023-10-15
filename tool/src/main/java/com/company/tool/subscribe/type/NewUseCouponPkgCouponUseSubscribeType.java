package com.company.tool.subscribe.type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.tool.subscribe.SubscribeType;
import com.company.tool.subscribe.SubscribeTypeBeanFactory;
import com.company.tool.subscribe.dto.SubscribeSendDto;
import com.google.common.collect.Lists;

@Component(SubscribeTypeBeanFactory.NEW_USER_COUPON_PKG_COUPON_USE)
public class NewUseCouponPkgCouponUseSubscribeType implements SubscribeType {

	@Override
	public SubscribeSendDto wrapParam(Integer userId, Map<String, String> runtimeAttach) {
		String page = "pages/mealList/index?shopCode=P7575409&autoAddId=299";
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
