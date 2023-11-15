package com.company.order.entpay;

import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.pay.dto.PayParams;

/**
 * 企业付款
 */
public interface EnterprisePaymentClient {
	/**
	 * 付款
	 * 
	 * @param payParams
	 *            支付参数
	 * @return
	 */
	PayResp entPay(PayParams payParams);

	/**
	 * 查询付款状态
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @return
	 */
	PayTradeStateResp queryEntPay(String outTradeNo);
}
