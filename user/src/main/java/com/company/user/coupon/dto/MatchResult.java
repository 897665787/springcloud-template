package com.company.user.coupon.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchResult {
	Boolean canUse;// 能用
	String reason;// 不可用原因(canUse=false有值)
}