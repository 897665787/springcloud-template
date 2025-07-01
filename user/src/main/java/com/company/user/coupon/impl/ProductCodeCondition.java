package com.company.user.coupon.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.user.coupon.FilterParam;
import com.company.user.coupon.UseCondition;
import com.company.user.coupon.UseParam;
import com.company.user.coupon.dto.MatchResult;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * 指定商品可用
 */
@Slf4j
@Component("ProductCodeCondition")
public class ProductCodeCondition implements UseCondition {

	@Override
	public MatchResult canUse(UseParam useParam) {
		Map<String, String> runtimeAttach = useParam.getRuntimeAttach();
		String productCode = runtimeAttach.get("productCode");
		String useConditionValue = useParam.getUseConditionValue();

		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String productCodes = JsonUtil.getString(useConditionJson, "productCodes");

		List<String> productCodeList = Arrays.asList(StringUtils.split(productCodes, ","));

		boolean canUse = productCodeList.contains(productCode);
		if (!canUse) {
			log.info("{}条件不满足,配置商品{},当前商品:{}", useParam.getUserCouponId(), productCodes, productCode);
			return MatchResult.builder().canUse(false).reason("仅部分商品指定可用").build();
		}
		return MatchResult.builder().canUse(true).build();
	}

	@Override
	public List<String> filterProduct(FilterParam filterParam) {
		String useConditionValue = filterParam.getUseConditionValue();
		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String productCodes = JsonUtil.getString(useConditionJson, "productCodes");

		List<String> productCodeList = Arrays.asList(StringUtils.split(productCodes, ","));
		return productCodeList;
	}
}
