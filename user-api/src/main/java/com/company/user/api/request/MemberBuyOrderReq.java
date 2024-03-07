package com.company.user.api.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberBuyOrderReq {
	// 商品参数
	private String productCode;
	private Integer number;
	private BigDecimal payAmount;

	// 支付方式
	private String payMethod;
	// 支付应用ID
	private String appid;
	// 使用的优惠券id
	private Integer userCouponId;

	// 充值号码
	private String rechargeNumber;
	
	private String userRemark;

}
