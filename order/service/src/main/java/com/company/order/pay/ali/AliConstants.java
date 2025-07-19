package com.company.order.pay.ali;

import com.alipay.api.AlipayConstants;

public interface AliConstants {

	String FORMAT = AlipayConstants.FORMAT_JSON;
	String SIGNTYPE = AlipayConstants.SIGN_TYPE_RSA2;
	String CHARSET = AlipayConstants.CHARSET_UTF8;
	
	public interface TradeStatus {
		String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";// 交易创建，等待买家付款
		String TRADE_SUCCESS = "TRADE_SUCCESS";// 交易支付成功
		String TRADE_CLOSED = "TRADE_CLOSED";// 未付款交易超时关闭，或支付完成后全额退款
		String TRADE_FINISHED = "TRADE_FINISHED";// 交易结束，不可退款
		
		static boolean payHasResult(String tradeStatus) {
			if (TRADE_SUCCESS.equals(tradeStatus)) {
				return true;
			}
			if (TRADE_CLOSED.equals(tradeStatus)) {
				return true;
			}
			if (TRADE_FINISHED.equals(tradeStatus)) {
				return true;
			}
			return false;
		}
		
		static boolean paySuccess(String tradeStatus) {
			if (TRADE_SUCCESS.equals(tradeStatus)) {
				return true;
			}
			if (TRADE_FINISHED.equals(tradeStatus)) {
				return true;
			}
			return false;
		}
	}

	public interface RefundStatus {
		String REFUND_SUCCESS = "REFUND_SUCCESS";

		static boolean refundHasResult(String refundStatus) {
			if (REFUND_SUCCESS.equals(refundStatus)) {
				return true;
			}
			return false;
		}
		
		static boolean refundSuccess(String refundStatus) {
			if (REFUND_SUCCESS.equals(refundStatus)) {
				return true;
			}
			return false;
		}
	}
}
