package com.company.order.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayCloseReq {
	/**
	 * 订单号<必填>
	 */
	@NotBlank(message = "订单号不能为空")
	private String orderCode;
}
