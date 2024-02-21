package com.company.order.api.response;

import com.company.order.api.enums.OrderPayEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayInfoResp {
	
	/**
	 * 支付方式
	 */
	private OrderPayEnum.Method method;

	/**
	 * 支付信息，前端使用
	 */
	private Object payInfo;

}
