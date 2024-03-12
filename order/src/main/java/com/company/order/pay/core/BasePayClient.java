package com.company.order.pay.core;

import java.math.BigDecimal;

import com.company.common.exception.BusinessException;
import com.company.order.api.response.PayCloseResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
import com.company.order.pay.dto.PayParams;

import lombok.extern.slf4j.Slf4j;

/**
 * 基础支付客户端
 */
@Slf4j
public abstract class BasePayClient implements PayClient {

	@Override
	public PayResp pay(PayParams payParams) {
		PayResp payResp = new PayResp();
		payResp.setOrderCode(payParams.getOutTradeNo());
		try {
			Object payInfo = requestPayInfo(payParams);
			payResp.setSuccess(true);
			payResp.setMessage("成功");
			payResp.setPayInfo(payInfo);
		} catch (BusinessException e) {
			payResp.setSuccess(false);
			payResp.setMessage(e.getMessage());
		}
		return payResp;
	}

	@Override
	public PayRefundResp refund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		PayRefundResp payRefundResp = new PayRefundResp();
		try {
			requestRefund(outTradeNo, outRefundNo, refundAmount);
			payRefundResp.setSuccess(true);
			payRefundResp.setMessage("成功");
		} catch (BusinessException e) {
			payRefundResp.setSuccess(false);
			payRefundResp.setMessage(e.getMessage());
		}
		return payRefundResp;
	}

	@Override
	public PayCloseResp payClose(String outTradeNo) {
		log.info("开始关闭订单 outTradeNo = {} ", outTradeNo);
		PayCloseResp payCloseResp = new PayCloseResp();
		try {
			requestPayCloseOrder(outTradeNo);
			payCloseResp.setSuccess(true);
			payCloseResp.setMessage("成功");
		} catch (BusinessException e) {
			payCloseResp.setSuccess(false);
			payCloseResp.setMessage(e.getMessage());
		}
		return payCloseResp;
	}

	protected abstract Object requestPayInfo(PayParams payParams);

	protected abstract void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount);

	/**
	 * 关闭订单
	 *
	 * @param outTradeNo 交易订单号
	 */
	protected abstract void requestPayCloseOrder(String outTradeNo);
}
