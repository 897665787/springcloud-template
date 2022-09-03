package com.company.order.pay.wx.result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信H5支付
 */
@Data
@AllArgsConstructor
public class WxPayMwebOrderResult {
	private String mwebUrl;
}
