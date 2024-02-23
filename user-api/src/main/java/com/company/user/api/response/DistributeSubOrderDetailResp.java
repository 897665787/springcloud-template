package com.company.user.api.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistributeSubOrderDetailResp {
	/* 与前端约定添加子订单特有的字段 */
	/**
	 * 是否展示评价按钮
	 */
	private Boolean commentBtn;
	/**
	 * 是否展示邀请按钮
	 */
	private Boolean inviteBtn;
	
	private String brandLogo;
	private String brandName;

	/**
	 * 取餐码
	 */
	private String mealCode;
	
	/**
	 * 商品列表
	 */
	private List<ProductDetailResp> productList;

	@Data
	public static class ProductDetailResp {
		/**
		 * 购买数量
		 */
		private Integer number;

		/**
		 * 原价
		 */
		private BigDecimal originAmount;

		/**
		 * 售价
		 */
		private BigDecimal salesAmount;

		/**
		 * 总额(售价*数量)
		 */
		private BigDecimal amount;

		/**
		 * 商品编码
		 */
		private String productCode;

		/**
		 * 商品名称
		 */
		private String productName;

		/**
		 * 商品小图
		 */
		private String productImage;
		
		/**
		 * 规格内容
		 */
		private String specContent;;
		
		/**
		 * 用户备注
		 */
		private String userRemark;
	}
	
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderDetailResp的字段值 */
	/**
	 * 状态文案
	 */
	private String statusText;

	// 订单信息（数组维度展示信息）
	private List<TextValueResp> textValueList;

	@Data
	@Accessors(chain = true)
	public static class TextValueResp {
		private String text;// 文本
		private String value;// 值
	}
	/* 如有需要，使用相同的字段名覆盖OrderDetailResp的字段值 */
}
