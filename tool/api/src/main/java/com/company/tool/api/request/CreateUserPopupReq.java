package com.company.tool.api.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.company.tool.api.enums.PopupEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
public class CreateUserPopupReq {
	
	/**
	 * 用户ID
	 */
	@NotNull
	Integer userId;
	
	/**
	 * 弹窗有效期开始时间
	 */
	@NotNull
	LocalDateTime beginTime;
	
	/**
	 * 弹窗有效期结束时间
	 */
	@NotNull
	LocalDateTime endTime;
	
	/**
	 * 优先级（值越大，优先级越高）
	 */
	@NotNull
	Integer priority;
	
	/**
	 * 标题
	 */
	String title;
	
	/**
	 * 文案
	 */
	String text;
	
	/**
	 * 背景图（不配置前端不展示）
	 */
	PopImage bgImg;
	/**
	 * 关闭按钮（不配置前端不展示）
	 */
	PopButton closeBtn;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class PopImage {
		
		@NotNull
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
