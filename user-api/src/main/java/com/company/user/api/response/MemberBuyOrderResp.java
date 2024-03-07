package com.company.user.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemberBuyOrderResp {
	// 是否需要进行支付
	private Boolean needPay;

	// 支付信息，needPay=true有值
	private Object payInfo;
}
