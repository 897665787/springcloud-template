package com.company.order.withdraw.core;

import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.pay.dto.PayParams;

/**
 * 提现客户端
 */
public interface WithdrawClient {
	String BEAN_NAME_PREFIX = "withdrawClient-";

	/**
	 * 提现
	 * 
	 * @param payParams
	 *            支付参数
	 * @return
	 */
	PayResp withdraw(PayParams payParams);

	/**
	 * 查询交易状态
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @return
	 */
	PayTradeStateResp queryTradeState(String outTradeNo);
}
