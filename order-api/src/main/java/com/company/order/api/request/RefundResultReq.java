package com.company.order.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RefundResultReq {
    /**
     * 退款订单号
     */
	@NotBlank(message = "退款订单号不能为空")
    private String refundOrderCode;
}
