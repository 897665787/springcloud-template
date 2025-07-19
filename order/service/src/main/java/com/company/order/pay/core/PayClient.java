package com.company.order.pay.core;

import java.math.BigDecimal;

import com.company.order.api.response.PayCloseResp;
import com.company.order.api.response.PayOrderQueryResp;
import com.company.order.api.response.PayRefundQueryResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
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
	 * 查询订单
	 * 
	 * @param outTradeNo
	 *            交易订单号
	 * @return
	 */
	default PayOrderQueryResp orderQuery(String outTradeNo) {
		PayOrderQueryResp resp = new PayOrderQueryResp();
		resp.setResult(false);
		resp.setMessage("未实现查询订单");
		return resp;
	}

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

	/**
	 * 查询退款
	 * 
	 * @param outRefundNo
	 *            退款订单号
	 * @return
	 */
	default PayRefundQueryResp refundQuery(String outRefundNo) {
		PayRefundQueryResp resp = new PayRefundQueryResp();
		resp.setResult(false);
		resp.setMessage("未实现查询退款");
		return resp;
	}
}
