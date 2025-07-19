package com.company.order.pay.aliactivity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRefundBizContent {

	@JsonProperty("id")
	String id;
	
	@JsonProperty("order_no")
	String orderNo;
	
	@JsonProperty("out_order_no")
	String outOrderNo;
	
	@JsonProperty("event_time")
	String eventTime;
	
	@JsonProperty("send_status")
	String sendStatus;
	
	@JsonProperty("refund_type")
	String refundType;
	
	@JsonProperty("out_biz_no")
	String outBizNo;
}
