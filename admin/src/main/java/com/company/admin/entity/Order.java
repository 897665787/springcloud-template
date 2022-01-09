package com.company.admin.entity;

import com.company.admin.entity.enums.OrderEnum;
import com.company.common.jackson.annotation.AutoDesc;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {
	private Long id;
	private String orderCode;

	@AutoDesc(value = OrderEnum.Type.class)
	private Integer type;
}
