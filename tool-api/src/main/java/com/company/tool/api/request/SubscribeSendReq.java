package com.company.tool.api.request;

import java.time.LocalDateTime;
import java.util.List;

import com.company.tool.api.enums.SubscribeEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribeSendReq {
	/**
	 * openid<必填>
	 */
	String openid;
	/**
	 * 跳转页面<非必填>
	 */
	String page;
	/**
	 * 参数列表<必填>
	 */
	List<String> valueList;
	/**
	 * 消息类型<必填>
	 */
	SubscribeEnum.Type type;
	/**
	 * 计划发送时间<必填>
	 */
	LocalDateTime planSendTime;
	/**
	 * 发送超时时间<非必填>
	 */
	LocalDateTime overTime;
}
