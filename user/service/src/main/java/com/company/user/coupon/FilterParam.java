package com.company.user.coupon;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterParam {
	Integer couponTemplateId;// 优惠券模板ID
	
	String useConditionValue;// 使用条件值
}