package com.company.openapi.req;

import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderReq {
	@NotEmpty(message = "订单号不能为空")
	String orderCode;

	@NotEmpty(message = "商品编码不能为空")
	String productCode;
}
