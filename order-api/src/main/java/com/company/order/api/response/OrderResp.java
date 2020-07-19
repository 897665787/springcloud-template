package com.company.order.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderResp {
	private Long id;
	private String orderCode;
}
