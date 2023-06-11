package com.company.user.coupon.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCouponCanUseParam {
	String uniqueCode;
	BigDecimal orderAmount;
	Map<String, String> runtimeAttach;
}