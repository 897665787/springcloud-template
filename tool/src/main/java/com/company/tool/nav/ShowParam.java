package com.company.tool.nav;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowParam {
	/* 必填参数 */
	/**
	 * 导航栏金刚位ID
	 */
	Integer navItemId;

	/* 运行时参数 */
	/**
	 * 运行时附带参数（一般是调用处和ShowCondition实现类约定好）
	 */
	Map<String, String> runtimeAttach;

	/* 配置参数 */
	/**
	 * 使用条件值
	 */
	String showConditionValue;
}