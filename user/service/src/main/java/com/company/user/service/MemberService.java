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
public class MemberService {

	@Data
	@Accessors(chain = true)
	public static class MemberData {
		String productCode;
		BigDecimal productAmount;
		Integer addDays;
	}
	
	private List<MemberData> testDataList = Lists.newArrayList(
			new MemberData().setProductCode("M_7").setProductAmount(new BigDecimal("10")).setAddDays(7),
			new MemberData().setProductCode("M_30").setProductAmount(new BigDecimal("30")).setAddDays(30),
			new MemberData().setProductCode("M_365").setProductAmount(new BigDecimal("300")).setAddDays(365)
			);
	private Map<String, MemberData> testDataMap = testDataList.stream().collect(Collectors.toMap(MemberData::getProductCode, a->a));

	public MemberData selectByProductCode(String productCode) {
		return testDataMap.get(productCode);
	}
}
