package com.company.tool.popup.dto;

import com.company.tool.api.enums.PopupEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopButton {
	/**
	 * 按钮类型
	 */
	PopupEnum.Type type;
	
	/**
	 * 文案
	 */
	String text;

	/**
	 * 值
	 */
	String value;
}