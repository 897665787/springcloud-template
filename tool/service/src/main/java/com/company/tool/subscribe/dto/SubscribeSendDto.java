package com.company.tool.subscribe.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribeSendDto {
	/**
	 * 跳转页面<非必填>
	 */
	String page;
	/**
	 * 参数列表<必填>
	 */
	List<String> valueList;
	/**
	 * 计划发送时间<必填>
	 */
	LocalDateTime planSendTime;
	/**
	 * 发送超时时间<非必填>
	 */
	LocalDateTime overTime;
}
