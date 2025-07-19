package com.company.user.api.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RechargeSubOrderResp {
	/* 与前端约定添加子订单特有的字段 */
	/**
	 * 充值金额
	 */
	private BigDecimal amount;
	/**
	 * 赠送金额
	 */
	private BigDecimal giftAmount;
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
}
