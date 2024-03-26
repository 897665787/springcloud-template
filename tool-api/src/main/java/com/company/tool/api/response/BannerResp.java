package com.company.tool.api.response;

import com.company.common.jackson.annotation.AutoDesc;
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
	@AutoDesc(value = BannerEnum.Type.class)
	BannerEnum.Type type;

	/**
	 * 跳转值
	 */
	String value;
}