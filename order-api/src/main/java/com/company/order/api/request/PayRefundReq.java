package com.company.order.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.order.api.enums.OrderPayRefundEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayRefundReq {
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
	 * 业务类型(user:用户申请,sys_auto:系统自动退款,amdin:管理后台申请)<必填>
	 */
	@NotNull(message = "业务类型不能为空")
	private OrderPayRefundEnum.BusinessType businessType;
	
	/**
	 * 退款金额(不传代表全额退款)<非必填>
	 */
	private BigDecimal refundAmount;

	/* ======================内部服务====================== */
	/**
	 * 回调url（微服务内部回调）<非必填>
	 * 
	 * <p>
	 * 例：
	 * <p>
	 * url：http://template-user/goods/refundNotify
	 * <p>
	 * 请求方式：POST(强制)
	 * <p>
	 * 参数：com.company.order.api.request.RefundNotifyReq
	 * 
	 */
	private String notifyUrl;
	
	/**
	 * 附加参数<非必填>
	 * <p>
	 * 回调notifyUrl中原样返回，可作为自定义参数使用
	 */
	private String attach;
	
	/**
	 * 退款备注，写到bu_user_pay_order的refund_remark字段
	 */
	private String refundRemark;
}
