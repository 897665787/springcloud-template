package com.company.order.entity;

import com.company.common.jackson.annotation.AutoDesc;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {
	private Long id;
	private String orderCode;
	
	@AutoDesc({ "1:权益", "2:购买会员" })
	private Integer type;
}
