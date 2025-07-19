package com.company.order.api.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderPaySuccessReq {
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 支付金额
	 */
	@NotNull(message = "支付金额不能为空")
	private BigDecimal payAmount;
	
	/**
	 * 支付时间
	 */
	@NotNull(message = "支付时间不能为空")
	private LocalDateTime payTime;
}
