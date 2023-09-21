package com.company.tool.popup;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopParam {
	/* 必填参数 */
	/**
	 * 弹窗ID
	 */
	Integer popupId;
	/**
	 * 用户ID（与 设备ID 2选1）
	 */
	Integer userId;
	/**
	 * 设备ID（与 用户ID 2选1）
	 */
	String deviceid;

	/* 运行时参数 */
	/**
	 * 运行时附带参数（一般是调用处和PopCondition实现类约定好）
	 */
	Map<String, String> runtimeAttach;

	/* 配置参数 */
	/**
	 * 使用条件值
	 */
	String popConditionValue;
}