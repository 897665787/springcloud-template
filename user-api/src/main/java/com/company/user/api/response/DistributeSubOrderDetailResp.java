package com.company.user.api.response;

import java.math.BigDecimal;
import java.util.List;

import com.company.common.jackson.annotation.FormatNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistributeSubOrderDetailResp {
	/* 与前端约定添加子订单特有的字段 */
	// 配送费
	@FormatNumber(pattern = "0.0#")
	BigDecimal distributeAmount;

	// 保温费
	@FormatNumber(pattern = "0.0#")
	BigDecimal baowenAmount;
	
	/**
	 * 取餐码
	 */
	private String mealCode;

	/**
	 * 门店列表（以门店分组展示）
	 */
	private List<ShopResp> shopList;
	
	@Data
	public static class ShopResp {
		String shopCode;
		String shopName;
		String shopLogo;
		
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

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShopResp other = (ShopResp) obj;
			if (shopCode == null) {
				if (other.shopCode != null)
					return false;
			} else if (!shopCode.equals(other.shopCode))
				return false;
			if (shopLogo == null) {
				if (other.shopLogo != null)
					return false;
			} else if (!shopLogo.equals(other.shopLogo))
				return false;
			if (shopName == null) {
				if (other.shopName != null)
					return false;
			} else if (!shopName.equals(other.shopName))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((shopCode == null) ? 0 : shopCode.hashCode());
			result = prime * result + ((shopLogo == null) ? 0 : shopLogo.hashCode());
			result = prime * result + ((shopName == null) ? 0 : shopName.hashCode());
			return result;
		}
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
	@AllArgsConstructor
	public static class TextValueResp {
		private String text;// 文本
		private String value;// 值
	}
	/* 如有需要，使用相同的字段名覆盖OrderDetailResp的字段值 */
}
