package com.company.tool.api.response;

import com.company.tool.api.enums.PopupEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
public class BestPopupResp  {
	/**
	 * 弹窗模板（前端定义）
	 */
	String model;

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
	
	/**
	 * 下一步（没有则不填）
	 * <pre>
	 * 使用场景：比如点击图片请求api，成功后展示另外一张图片，点击图片跳转（比如领取优惠券成功后去使用）
	 * </pre>
	 */
	BestPopupResp next;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class PopImage {
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
	}
	
	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class PopButton {
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
}