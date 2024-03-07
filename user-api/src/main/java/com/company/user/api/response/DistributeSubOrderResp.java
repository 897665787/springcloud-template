package com.company.user.api.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.company.common.jackson.annotation.FormatNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistributeSubOrderResp {
	/* 与前端约定添加子订单特有的字段 */
	
	private Integer totalNumber;
	// 第一个商品
	private String productCode;
	private String productName;
	private String productImage;
	
	// 第一个商品的门店
	String shopCode;
	String shopName;
	String shopLogo;
	
	/**
	 * 取餐码
	 */
	private String mealCode;

	// 配送费
	@FormatNumber(pattern = "0.0#")
	BigDecimal distributeAmount;

	// 保温费
	@FormatNumber(pattern = "0.0#")
	BigDecimal baowenAmount;

	/**
	 * 是否展示评价按钮
	 */
	private Boolean commentBtn;
	/**
	 * 是否展示邀请按钮
	 */
	private Boolean inviteBtn;
	
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
	/**
	 * 状态文案
	 */
	private String statusText;

	/**
	 * 时间文案
	 */
	private String timeText;

	/**
	 * 时间
	 */
	private Date time;

	/**
	 * 按钮列表(超过n个按钮则放入更多，前端控制)
	 */
	private List<BottonResp> bottonList;

	@Data
	@AllArgsConstructor
	public static class BottonResp {
		/**
		 * 文案
		 */
		private String text;
		
		/**
		 * 点击后重定向页面
		 */
		private String url;
		
		/**
		 * 排序（从右到左，值小到大）
		 */
		private Integer sort;
	}
	
	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
}
