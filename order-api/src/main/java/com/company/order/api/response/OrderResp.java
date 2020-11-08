package com.company.order.api.response;

import com.company.common.jackson.annotation.AutoDesc;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderResp {
	private Long id;
	private String orderCode;
	private Integer port;
	
	@AutoDesc({ "1:权益", "2:购买会员" })
	private Integer type;
}
