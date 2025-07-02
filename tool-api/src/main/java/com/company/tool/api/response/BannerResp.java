package com.company.tool.api.response;

import com.company.tool.api.enums.BannerEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerResp {
	/**
	 * 标题
	 */
	String title;

	/**
	 * 图
	 */
	String image;

	/**
	 * 跳转类型
	 */
	BannerEnum.Type type;

	/**
	 * 跳转值
	 */
	String value;
}