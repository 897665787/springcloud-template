package com.company.user.api.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistributeSubOrderResp {
	/* 与前端约定添加子订单特有的字段 */
	/**
	 * 是否展示评价按钮
	 */
	private Boolean commentBtn;
	/**
	 * 是否展示邀请按钮
	 */
	private Boolean inviteBtn;
	
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
	BigDecimal distributeAmount;

	// 保温费
	BigDecimal baowenAmount;

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
	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
}
