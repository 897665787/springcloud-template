package com.company.order.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.company.common.jackson.annotation.FormatNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderResp {
	/**
	 * 订单号
	 */
	private String orderCode;
	/**
	 * 业务类型 关联对应业务表
	 */
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
	 * 时间文案
	 */
	private String timeText;
	
	/**
	 * 时间
	 */
	private LocalDateTime time;
	
	/**
	 * 是否展示‘删除订单’按钮
	 */
	private Boolean deleteBtn;
	
	/**
	 * 是否展示‘取消订单’按钮
	 */
	private Boolean cancelBtn;

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
		 * 点击后重定向页面(前端根据该key做路径映射)
		 */
		private String key;
		
		/**
		 * 参数
		 */
		private String params;
		
		/**
		 * 排序（从右到左，值小到大）
		 */
		private Integer sort;
	}
	
	/**
	 * 商品列表
	 */
	private List<ProductResp> productList;
	
	/**
	 * 子订单
	 */
	private Object subOrder;

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
