package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.experimental.Accessors;

@Component
public class ShopCartService {

	@Data
	@Accessors(chain = true)
	public static class ShopCart {
//		String cartType;
		Integer userId;
		
		Integer number;
		BigDecimal originAmount;
		BigDecimal salesAmount;
		
//		String uniqueCode;
		String productCode;
		String productName;
		String productImage;
//		String specJson;
		String specContent;
	}

	private List<ShopCart> testDataList = Lists.newArrayList(
			new ShopCart().setNumber(1).setOriginAmount(new BigDecimal("10")).setSalesAmount(new BigDecimal("10")).setProductCode("111111").setProductName("商品1").setProductImage("https://iamge.com/aaa/bbb.png").setSpecContent("辣/大"),
			new ShopCart().setNumber(2).setOriginAmount(new BigDecimal("20")).setSalesAmount(new BigDecimal("20")).setProductCode("111112").setProductName("商品2").setProductImage("https://iamge.com/aaa/bbb.png").setSpecContent("大"),
			new ShopCart().setNumber(1).setOriginAmount(new BigDecimal("10")).setSalesAmount(new BigDecimal("8")).setProductCode("111113").setProductName("商品3").setProductImage("https://iamge.com/aaa/bbb.png").setSpecContent("")
			);

	public List<ShopCart> selectByUserId(Integer userId) {
		return testDataList;
	}
}
