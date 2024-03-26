package com.company.tool.nav.param;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.nav.NavReplaceParam;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 商品替换
 */
@Component("{goods}")
public class GoodsReplaceParam implements NavReplaceParam {

	@Override
	public String replace() {
		List<String> goodsTemplateList = Lists.newArrayList();

		List<Goods> newUserCouponList = goodsTemplateList.stream().map(v -> {
			Goods goods = new Goods();
			// goods.setCode(v.getXXX());
			// goods.setName(v.getXXX());
			// goods.setLogo(v.getXXX());
			return goods;
		}).collect(Collectors.toList());

		return JsonUtil.toJsonString(newUserCouponList);
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	private static class Goods {
		String code;
		String name;
		String logo;
	}
}