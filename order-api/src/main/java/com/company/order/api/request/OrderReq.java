package com.company.order.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderReq {
	private Long id;
	private String orderCode;
	private Long seq;
}
