package com.company.app.req;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundApplyReq {
	@NotBlank(message = "订单号不能为空")
	String orderCode;
	
	/**
	 * 退款原因
	 */
	private String refundReason;
}
