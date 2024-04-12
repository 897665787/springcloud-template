package com.company.order.api.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRefundFinishReq {
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 退款完成时间
	 */
	@NotNull(message = "退款完成时间不能为空")
	private LocalDateTime refundFinishTime;
	
	/**
	 * 总退款金额
	 */
	@NotNull(message = "总退款金额不能为空")
	private BigDecimal totalRefundAmount;
	
	/**
	 * 是否全额退款（由调用方判断，例如退款时扣手续费，不全退也算全额退款）
	 */
	@NotNull(message = "是否全额退款不能为空")
	private Boolean refundAll;
}
