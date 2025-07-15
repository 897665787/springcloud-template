package com.company.tool.api.response;

import com.company.tool.api.enums.NavItemEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavResp {
	/**
	 * 标题
	 */
	String title;

	/**
	 * LOGO图
	 */
	String logo;

	/**
	 * 跳转类型
	 */
	NavItemEnum.Type type;

	/**
	 * 跳转值
	 */
	String value;

	/**
	 * 附加json对象（透传）
	 */
	Object attach = new Object();
}