package com.company.user.coupon.dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCouponCanUse {
	Integer userCouponId;// 用户优惠券ID
	BigDecimal reduceAmount;// 优惠金额
	Boolean canUse;// 能用
	String reason;// 不可用原因(canUse=false有值)
}