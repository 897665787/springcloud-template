package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.experimental.Accessors;

@Component
public class ChargeConfigService {

	@Data
	@Accessors(chain = true)
	public static class ChargeGiftData {
		String rechargeCode;
		BigDecimal chargeAmount;
		BigDecimal giftAmount;
	}

	private List<ChargeGiftData> testDataList = Lists.newArrayList(
			new ChargeGiftData().setRechargeCode("R1").setChargeAmount(new BigDecimal("100"))
					.setGiftAmount(new BigDecimal("10")),
			new ChargeGiftData().setRechargeCode("R2").setChargeAmount(new BigDecimal("200"))
					.setGiftAmount(new BigDecimal("30")),
			new ChargeGiftData().setRechargeCode("R3").setChargeAmount(new BigDecimal("300"))
					.setGiftAmount(new BigDecimal("50")));
	private Map<String, ChargeGiftData> testDataMap = testDataList.stream()
			.collect(Collectors.toMap(ChargeGiftData::getRechargeCode, a -> a));

	public ChargeGiftData selectByRechargeCode(String rechargeCode) {
		return testDataMap.get(rechargeCode);
	}
}
