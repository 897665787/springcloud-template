package com.company.tool.popup.dto;

import com.company.tool.api.enums.PopupEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopupCanPop {
	PopupEnum.LogBusinessType businessType;// 业务类型
	Integer businessId;//业务ID

	Integer priority;// 优先级

	/**
	 * 标题
	 */
	String title;

	/**
	 * 文案
	 */
	String text;

	/**
	 * 背景图
	 */
	PopImage bgImg;

	/**
	 * 关闭按钮
	 */
	PopButton closeBtn;

	Integer popupLogId;// 弹窗记录ID
}