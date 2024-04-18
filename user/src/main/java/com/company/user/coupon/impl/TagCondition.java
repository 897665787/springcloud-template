package com.company.user.coupon.impl;

import java.util.List;
import java.util.Map;

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
 * 某标签下的商品可用
 */
@Slf4j
@Component("TagCondition")
public class TagCondition implements UseCondition {

	Map<String, List<String>> mockTagProductListMap = Maps.newHashMap();
	{
		mockTagProductListMap.put("tag1", Lists.newArrayList("AB3301", "AB3302"));
		mockTagProductListMap.put("tag2", Lists.newArrayList("AB3302", "AB3304"));
		mockTagProductListMap.put("tag3", Lists.newArrayList("AB3302", "AB3304", "AB3305"));
	}

	@Override
	public MatchResult canUse(UseParam useParam) {
		Map<String, String> runtimeAttach = useParam.getRuntimeAttach();
		String productCode = runtimeAttach.get("productCode");
		String useConditionValue = useParam.getUseConditionValue();

		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String tag = JsonUtil.getString(useConditionJson, "tag");

		boolean canUse = mockTagProductListMap.get(tag).contains(productCode);
		if (!canUse) {
			log.info("{}条件不满足,标签“{}”不包含商品“{}”", useParam.getUserCouponId(), tag, productCode);
			return MatchResult.builder().canUse(false).reason("商品条件不满足").build();
		}
		return MatchResult.builder().canUse(true).build();
	}

	@Override
	public List<String> filterProduct(FilterParam filterParam) {
		// 根据标签查询商品
		String useConditionValue = filterParam.getUseConditionValue();

		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String tag = JsonUtil.getString(useConditionJson, "tag");

		List<String> productCodeList = mockTagProductListMap.get(tag);
		return productCodeList;
	}
}