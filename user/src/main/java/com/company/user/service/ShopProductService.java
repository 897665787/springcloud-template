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
public class ShopProductService {

	@Data
	@Accessors(chain = true)
	public static class ShopProduct {
		String productCode;
		String productName;
		String productImage;
		
		BigDecimal originAmount;
		BigDecimal salesAmount;
		
		String shopCode;
	}

	private List<ShopProduct> testDataList = Lists.newArrayList(
			new ShopProduct().setOriginAmount(new BigDecimal("10")).setSalesAmount(new BigDecimal("10")).setProductCode("111111").setProductName("商品1").setProductImage("https://iamge.com/aaa/bbb.png").setShopCode("S001"),
			new ShopProduct().setOriginAmount(new BigDecimal("20")).setSalesAmount(new BigDecimal("20")).setProductCode("111112").setProductName("商品2").setProductImage("https://iamge.com/aaa/bbb.png").setShopCode("S001"),
			new ShopProduct().setOriginAmount(new BigDecimal("10")).setSalesAmount(new BigDecimal("8")).setProductCode("111113").setProductName("商品3").setProductImage("https://iamge.com/aaa/bbb.png").setShopCode("S002")
			);

	private Map<String, ShopProduct> testDataMap = testDataList.stream().collect(Collectors.toMap(ShopProduct::getProductCode, a -> a));

	public ShopProduct selectByProductCode(String productCode) {
		ShopProduct shopProduct = testDataMap.get(productCode);
		return shopProduct;
	}
}
