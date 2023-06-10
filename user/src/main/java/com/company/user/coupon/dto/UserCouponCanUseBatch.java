package com.company.user.coupon.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCouponCanUseBatch {
	String uniqueCode;
	UserCouponCanUse userCouponCanUse;
}