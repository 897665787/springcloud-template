package com.company.order.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {
	private Long id;
	private String orderCode;
}
