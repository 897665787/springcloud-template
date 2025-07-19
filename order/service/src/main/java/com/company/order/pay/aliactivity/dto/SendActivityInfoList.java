package com.company.order.pay.aliactivity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendActivityInfoList {
	Integer quantity;

	@JsonProperty("activity_id")
	String activityId;
}
