package com.company.order.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefundNotifyReq {
	/**
	 * 成功
	 */
	private Boolean success;
	/**
	 * 信息
	 */
	private String message;
	
	/**
	 * 订单号<必填>
	 */
	@NotBlank(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 退款订单号<必填>
	 */
	@NotBlank(message = "退款订单号不能为空")
	private String refundOrderCode;

	/**
	 * 附加参数<非必填>
	 * <p>
	 * 回调notifyUrl中原样返回，可作为自定义参数使用
	 */
	private String attach;
	
	/**
	 * 本次退款金额<success=true时必填>
	 */
	private BigDecimal thisRefundAmount;
	
	/**
	 * 总退款金额<success=true时必填>
	 */
	private BigDecimal totalRefundAmount;
}
