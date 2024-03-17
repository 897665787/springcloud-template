package com.company.order.api.request;

import javax.validation.constraints.NotNull;

import com.company.order.api.enums.OrderEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRefundRejectReq {
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 退款申请旧状态
	 */
	@NotNull(message = "退款申请旧状态不能为空")
	private OrderEnum.SubStatusEnum oldSubStatus;
	
	/**
	 * 拒绝原因
	 */
	private String rejectReason;
}
