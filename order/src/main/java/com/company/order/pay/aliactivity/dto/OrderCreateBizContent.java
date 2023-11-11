package com.company.order.pay.aliactivity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateBizContent {
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

}
