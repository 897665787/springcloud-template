package com.company.order.api.request;

import lombok.Data;

@Data
public class RefundReq {

    /**
     * 退款订单号
     */
    private String refundOrderCode;

}
