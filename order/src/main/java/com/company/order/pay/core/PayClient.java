package com.company.order.pay.core;

import java.math.BigDecimal;

import com.company.order.api.response.PayCloseResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.pay.dto.PayParams;

/**
 * 支付客户端
 */
public interface PayClient {
	String BEAN_NAME_PREFIX = "payClient-";

	/**
	 * 支付
	 * 
	 * @param payParams
	 *            支付参数
	 * @return
	 */
	PayResp pay(PayParams payParams);

	/**
	 * 关闭订单
	 *
	 * @param outTradeNo 交易订单号
	 * @return
	 */
	PayCloseResp payClose(String outTradeNo);
	
	/**
	 * 获取支付信息
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @return
	 */
	Object getPayInfo(String outTradeNo);

	/**
	 * 查询交易状态
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @return
	 */
	PayTradeStateResp queryTradeState(String outTradeNo);

	/**
	 * 退款（部分/全额）
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @param outRefundNo
	 *            退款订单号
	 * @param refundAmount
	 *            退款金额
	 * @return
	 */
	PayRefundResp refund(String outTradeNo, String outRefundNo, BigDecimal refundAmount);
}
