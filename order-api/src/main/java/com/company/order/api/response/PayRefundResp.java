package com.company.order.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayRefundResp {
	/**
	 * 成功
	 */
	private Boolean success;
	/**
	 * 信息
	 */
	private String message;
}
