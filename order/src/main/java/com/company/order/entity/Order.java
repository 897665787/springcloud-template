package com.company.order.entity;

import com.company.common.jackson.annotation.AutoDesc;
import com.company.order.api.enums.OrderType;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {
	private Long id;
	private String orderCode;
	
	@AutoDesc(value = OrderType.class, code = "type")
	private Integer type;
}
