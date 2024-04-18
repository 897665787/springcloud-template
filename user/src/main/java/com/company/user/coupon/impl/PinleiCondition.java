package com.company.user.coupon.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.user.coupon.FilterParam;
import com.company.user.coupon.UseCondition;
import com.company.user.coupon.UseParam;
import com.company.user.coupon.dto.MatchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 某商品品类可用
 */
@Slf4j
@Component("PinleiCondition")
public class PinleiCondition implements UseCondition {

	// 模拟数据库存储的商品品类数据，key是商品编码，value是品类编码
	Map<String, String> mockProductCodePinleiMap = Maps.newHashMap();
	{
		mockProductCodePinleiMap.put("AB3301", "yifu");
		mockProductCodePinleiMap.put("AB3302", "yifu");
		mockProductCodePinleiMap.put("AB3303", "yifu");

		mockProductCodePinleiMap.put("AB3401", "niunai");
		mockProductCodePinleiMap.put("AB3402", "niunai");
		mockProductCodePinleiMap.put("AB3403", "niunai");

		mockProductCodePinleiMap.put("AB3501", "chaju");
		mockProductCodePinleiMap.put("AB3502", "chaju");
		mockProductCodePinleiMap.put("AB3503", "chaju");
	}

	@Override
	public MatchResult canUse(UseParam useParam) {
		Map<String, String> runtimeAttach = useParam.getRuntimeAttach();
		String productCode = runtimeAttach.get("productCode");

		if (StringUtils.isBlank(productCode)) {
			log.info("{}条件不满足,当前商品:{}", useParam.getUserCouponId(), productCode);
			return MatchResult.builder().canUse(false).reason("仅部分商品品类可用").build();
		}

		String useConditionValue = useParam.getUseConditionValue();
		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String pinleis = JsonUtil.getString(useConditionJson, "pinleis");

		String pinlei = mockProductCodePinleiMap.get(productCode);

		List<String> pinleiList = Arrays.asList(StringUtils.split(pinleis, ","));

		boolean canUse = pinleiList.contains(pinlei);
		if (!canUse) {
			log.info("{}条件不满足,配置商品品类{},当前商品品类:{}", useParam.getUserCouponId(), pinleis, pinlei);
			return MatchResult.builder().canUse(false).reason("仅部分商品品类可用").build();
		}
		return MatchResult.builder().canUse(true).build();
	}

	@Override
	public List<String> filterProduct(FilterParam filterParam) {
		String useConditionValue = filterParam.getUseConditionValue();
		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String pinleis = JsonUtil.getString(useConditionJson, "pinleis");

		List<String> pinleiList = Arrays.asList(StringUtils.split(pinleis, ","));

		List<String> productCodeList = Lists.newArrayList();
		for (String pinlei : pinleiList) {
			// 根据品类查询商品
			Set<Entry<String, String>> entrySet = mockProductCodePinleiMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();

				if (value.equals(pinlei)) {
					productCodeList.add(key);
				}
			}
		}
		return productCodeList;
	}
}