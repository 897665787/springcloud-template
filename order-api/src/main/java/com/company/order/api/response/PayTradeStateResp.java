package com.company.order.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayTradeStateResp {
	/**
	 * 有结果<必填>
	 */
	private Boolean result;

	/**
	 * 信息
	 */
	private String message;
	
	/**
	 * 支付成功
	 */
	private Boolean paySuccess;
}
