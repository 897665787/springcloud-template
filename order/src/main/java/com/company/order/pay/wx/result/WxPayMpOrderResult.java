package com.company.order.pay.wx.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

/**
 * 微信小程序支付
 */
@Data
@Builder
public class WxPayMpOrderResult {
	private String appId;
	private String timeStamp;
	private String nonceStr;
	/**
	 * 由于package为java保留关键字，因此改为packageValue. 响应json时更改为package
	 */
	@JsonProperty("package")
	private String packageValue;
	private String signType;
	private String paySign;
}
