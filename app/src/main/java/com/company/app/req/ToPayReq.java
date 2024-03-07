package com.company.app.req;

import javax.validation.constraints.NotBlank;

import com.company.order.api.enums.OrderPayEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ToPayReq {
	@NotBlank(message = "订单号不能为空")
	String orderCode;

	/**
	 * 支付方式，1：支付宝；2：微信；3：银行卡<必填>
	 */
	OrderPayEnum.Method method;
	
	/**
	 * 支付应用ID
	 */
	private String appid;
}
