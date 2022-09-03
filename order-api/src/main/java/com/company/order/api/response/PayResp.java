package com.company.order.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayResp {
	/**
	 * 成功
	 */
	private Boolean success;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 订单号
	 */
	private String orderCode;

	/**
	 * 支付信息，前端使用
	 */
	private Object payInfo;
}
