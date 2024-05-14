package com.company.order.api.response;

import java.math.BigDecimal;

import com.company.order.api.enums.OrderEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRefundApplyResp {
	/**
	 * 旧状态
	 */
	private OrderEnum.SubStatusEnum oldSubStatus;
	
	/**
	 * 可退款金额
	 */
	private BigDecimal canRefundAmount;
	
	/**
	 * 附加信息
	 */
	private String attach;
}