package com.company.order.pay.aliactivity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponReceivedBizContent {
	@JsonProperty("id")
	String id;

	@JsonProperty("activity_id")
	String activityId;

	@JsonProperty("event_time")
	String eventTime;

	@JsonProperty("voucher_id")
	String voucherId;

	@JsonProperty("voucher_code")
	String voucherCode;

	@JsonProperty("receive_user_id")
	String receiveUserId;

}
