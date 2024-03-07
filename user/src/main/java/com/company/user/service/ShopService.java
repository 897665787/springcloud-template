package com.company.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.experimental.Accessors;

@Component
public class ShopService {

	@Data
	@Accessors(chain = true)
	public static class Shop {
		String shopCode;
		String shopName;
		String shopLogo;
	}
	
	private List<Shop> testDataList = Lists.newArrayList(
			new Shop().setShopCode("S001").setShopName("门店1").setShopLogo("https://iamge.com/aaa/bbb.png"),
			new Shop().setShopCode("S002").setShopName("门店2").setShopLogo("https://iamge.com/aaa/bbb.png"),
			new Shop().setShopCode("S003").setShopName("门店3").setShopLogo("https://iamge.com/aaa/bbb.png")
			);

	private Map<String, Shop> testDataMap = testDataList.stream().collect(Collectors.toMap(Shop::getShopCode, a -> a));

	public Shop selectByShopCode(String shopCode) {
		return testDataMap.get(shopCode);
	}
}
