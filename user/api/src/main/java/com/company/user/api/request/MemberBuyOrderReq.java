package com.company.user.api.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberBuyOrderReq {
	// 商品参数
	private String productCode;
	private Integer number;

	// 钱包充值参数
	private String rechargeCode;
	private BigDecimal rechargeAmount;
	private BigDecimal walletPayAmount;
	
	// 支付参数
	private BigDecimal payAmount;
	private String payMethod; // 支付方式
	private String appid; // 支付应用ID
	
	// 使用的优惠券id
	private Integer userCouponId;

	// 充值号码
	private String rechargeNumber;
	
	private String userRemark;

}
