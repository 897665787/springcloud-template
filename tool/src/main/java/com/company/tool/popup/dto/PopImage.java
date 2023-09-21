package com.company.tool.popup.dto;

import com.company.tool.api.enums.PopupEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopImage {
	/**
	 * 弹窗模板（前端定义）
	 */
	PopupEnum.Model model;
	
	/**
	 * 图片链接
	 */
	String imgUrl;

	/**
	 * 按钮类型
	 */
	PopupEnum.Type type;

	/**
	 * 值
	 */
	String value;
	
	/**
	 * 下一步（没有则不填）
	 * <pre>
	 * 使用场景：比如点击图片请求api，成功后展示另外一张图片，点击图片跳转（比如领取优惠券成功后去使用）
	 * </pre>
	 */
	PopImage next;
}