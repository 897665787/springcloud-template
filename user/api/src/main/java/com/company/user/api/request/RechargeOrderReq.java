package com.company.user.api.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RechargeOrderReq {
	// 商品参数
	private BigDecimal rechargeAmount;
	private BigDecimal giftAmount;
	private BigDecimal payAmount;

	// 支付方式
	private String payMethod;
	// 支付应用ID
	private String appid;
}
