package com.company.tool.nav.dto;

import com.company.tool.api.enums.NavItemEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavItemCanShow {
	/**
	 * 标题
	 */
	String title;

	/**
	 * LOGO图
	 */
	String logo;

	NavItemEnum.Type type;

	String value;

	String attachJson;
}