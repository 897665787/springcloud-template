package com.company.order.pay.wx.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

/**
 * 微信APP支付
 */
@Data
@Builder
public class WxPayAppOrderResult {
	private String sign;
	private String prepayId;
	private String partnerId;
	private String appId;
	/**
	 * 由于package为java保留关键字，因此改为packageValue. 响应json时更改为package
	 */
	@JsonProperty("package")
	private String packageValue;
	private String timeStamp;
	private String nonceStr;
}