package com.company.tool.banner.dto;

import com.company.tool.api.enums.BannerEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerCanShow {
	/**
	 * 标题
	 */
	String title;

	/**
	 * 图
	 */
	String image;

	BannerEnum.Type type;

	String value;
}