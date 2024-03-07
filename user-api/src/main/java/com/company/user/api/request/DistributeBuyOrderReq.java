package com.company.user.api.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class DistributeBuyOrderReq {
	// 商品参数
	private BigDecimal payAmount;

	// 支付方式
	private String payMethod;
	// 支付应用ID
	private String appid;
	// 使用的优惠券id
	private Integer userCouponId;

	private List<UserRemarkReq> userRemarkList;

	@Data
	public static class UserRemarkReq {
		private String productCode;
		private String userRemark;
	}
}
