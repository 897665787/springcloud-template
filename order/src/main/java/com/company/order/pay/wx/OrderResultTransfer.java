package com.company.order.pay.wx;

public interface OrderResultTransfer {
	String BEAN_NAME_PREFIX = "orderResultTransfer-";

	/**
	 * 转换为支付信息
	 * 
	 * @param appid
	 * @param mchId
	 * @param prepayId
	 * @param codeUrl
	 * @param mwebUrl
	 * @return
	 */
	Object toPayInfo(String appid, String mchId, String prepayId, String codeUrl, String mwebUrl);
}
