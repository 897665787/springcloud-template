package com.company.order.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.order.api.enums.PayRefundApplyEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayRefundApplyReq {
	/**
	 * 退款订单号
	 */
	@NotBlank(message = "退款订单号不能为空")
	private String orderCode;

	/**
	 * 原订单号
	 */
	@NotBlank(message = "原订单号不能为空")
	private String oldOrderCode;
	
	/**
	 * 退款金额(元)
	 */
	private BigDecimal amount;

	/**
	 * 业务类型(USER:用户申请,SYS_AUTO:系统自动退款,AMDIN:管理后台申请)
	 */
	@NotNull(message = "业务类型不能为空")
	private PayRefundApplyEnum.BusinessType businessType;
	
	/**
	 * 审核状态(1:待审核,21:通过,22:驳回)
	 */
	@NotNull(message = "审核状态不能为空")
	private PayRefundApplyEnum.VerifyStatus verifyStatus;
	
	/**
	 * 退款原因
	 */
	private String reason;
	
	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 备注信息
	 */
	private String remark;
}