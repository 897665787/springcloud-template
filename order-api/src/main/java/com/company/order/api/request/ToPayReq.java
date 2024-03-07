package com.company.order.api.request;

import javax.validation.constraints.NotBlank;

import com.company.order.api.enums.OrderPayEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ToPayReq {
	/**
	 * 订单号<必填>
	 */
	@NotBlank(message = "订单号不能为空")
	private String orderCode;

	/**
	 * 支付方式，1：支付宝；2：微信；3：银行卡<必填>
	 */
	private OrderPayEnum.Method method;
	
	/**
	 * 支付应用ID
	 */
	private String appid;
	
	/**
	 * 终端IP<必填>
	 */
	@NotBlank(message = "终端IP不能为空")
	private String spbillCreateIp;

	/**
	 * 用户标识<非必填>
	 */
	private String openid;
}
