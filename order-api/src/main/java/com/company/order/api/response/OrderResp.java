package com.company.order.api.response;

import com.company.common.jackson.annotation.AutoDesc;
import com.company.order.api.enums.OrderType;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderResp {
	private Long id;
	private String orderCode;
	private Integer port;
	
	@AutoDesc(value = OrderType.class, code = "type")
	private Integer type;
}
