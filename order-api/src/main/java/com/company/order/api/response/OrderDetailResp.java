package com.company.order.api.response;

import java.math.BigDecimal;
import java.util.List;

import com.company.common.jackson.annotation.AutoDesc;
import com.company.common.jackson.annotation.FormatNumber;
import com.company.order.api.enums.OrderEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderDetailResp {
	/**
	 * 订单号
	 */
	private String orderCode;
	/**
	 * 业务类型 关联对应业务表
	 */
	@AutoDesc(value = OrderEnum.OrderType.class)
	private String orderType;
	/**
	 * 状态文案
	 */
	private String statusText;

    /**
     * 付款文案
     */
    private String payText;
	
    /**
     * 实付金额
     */
    @FormatNumber(pattern = "0.0#")
    private BigDecimal payAmount;
	
	/**
	 * 是否展示取消订单按钮
	 */
	private Boolean cancelBtn;
	/**
	 * 是否展示去付款按钮
	 */
	private Boolean toPayBtn;// 去付款信息通过单独的接口获取

	/**
	 * 商品列表
	 */
	private List<ProductResp> productList;

	// 订单信息（数组维度展示信息）
	private List<TextValueResp> textValueList;

	@Data
	@AllArgsConstructor
	public static class TextValueResp {
		private String text;// 文本
		private String value;// 值
	}

	/**
	 * 子订单
	 */
	private Object subOrder;

	/**
	 * 订单表
	 */
	@Data
	public static class ProductResp {
		/**
		 * 购买数量
		 */
		private Integer number;

		/**
		 * 原价
		 */
		@FormatNumber(pattern = "0.0#")
		private BigDecimal originAmount;

		/**
		 * 售价
		 */
		@FormatNumber(pattern = "0.0#")
		private BigDecimal salesAmount;

		/**
		 * 总额(售价*数量)
		 */
		@FormatNumber(pattern = "0.0#")
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
	}
}
