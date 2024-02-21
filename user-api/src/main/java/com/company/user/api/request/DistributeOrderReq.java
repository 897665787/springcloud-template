package com.company.user.api.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DistributeOrderReq {
	// 公共参数
	private BigDecimal amount;
	private Integer payMethod;
	private Integer platform;

	private String goodsNo;
	private Integer number;
	private BigDecimal realAmt;
	private BigDecimal orderAmount;
	private BigDecimal cardAmount;
	private BigDecimal payAmount;
	private Integer couponInfoId;
	private Integer newUserGift;// 新手礼包功能已废弃，该字段废弃

	private String openid;

	private String clientIp;

	// 卡券参数

	// 直充参数
	private String rechargeNumber;
	
	private Integer reqSource;
    private Integer reqPlatform;
	/**
	 * 订单类型
	 */
	private Integer orderType;

}
