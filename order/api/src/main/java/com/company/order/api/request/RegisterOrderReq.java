package com.company.order.api.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.company.order.api.enums.OrderEnum;

import lombok.Data;

@Data
public class RegisterOrderReq {
	/**
	 * bu_user_info.id
	 */
	@NotNull(message = "userId不能为空")
	private Integer userId;
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;
	/**
	 * 订单类型(每个子业务唯一)
	 */
	@NotNull(message = "订单类型不能为空")
	private String orderType;
	/**
	 * 订单子状态
	 */
	@NotNull(message = "订单状态不能为空")
	private OrderEnum.SubStatusEnum subStatusEnum;
	/**
	 * 商品总额
	 */
	@NotNull(message = "商品总额不能为空")
	private BigDecimal productAmount;
	/**
	 * 订单总额(元，商品金额+各种费用总和)
	 */
	@NotNull(message = "订单总额不能为空")
	private BigDecimal orderAmount;
	/**
	 * 抵扣总额(元，各种优惠、扣减总和)
	 */
	private BigDecimal reduceAmount = BigDecimal.ZERO;
	/**
	 * 需付金额(元)
	 */
	@NotNull(message = "需付金额不能为空")
	private BigDecimal needPayAmount;
	/**
	 * 子订单查询url
	 */
	private String subOrderUrl;

	/**
	 * 附加参数
	 */
	private String attach;
	
	/**
	 * 商品列表
	 */
	private List<OrderProductReq> productList;

	@Data
	public static class OrderProductReq {
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
		 * 附加参数
		 */
		private String attach;
	}
}
