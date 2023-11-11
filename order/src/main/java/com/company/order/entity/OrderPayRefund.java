package com.company.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("bu_order_pay_refund")
public class OrderPayRefund {

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * bu_user_info.id
	 */
	@TableField("user_id")
	private Integer userId;
	/**
	 * 退款订单号
	 */
	@TableField("refund_order_code")
	private String refundOrderCode;
	/**
	 * 订单号
	 */
	@TableField("order_code")
	private String orderCode;
	/**
	 * 业务类型(user:用户申请,sys_auto:系统自动退款,amdin:管理后台申请)
	 */
	@TableField("business_type")
	private String businessType;
	/**
	 * 支付方式(ali:支付宝,wx:微信,ios:苹果,quick:云闪付)
	 */
	@TableField("method")
	private String method;
	/**
	 * 金额(元)
	 */
	@TableField("amount")
	private BigDecimal amount;
	/**
	 * 退款金额(元)
	 */
	@TableField("refund_amount")
	private BigDecimal refundAmount;
	/**
	 * 状态(wait_apply:待申请,apply_success:申请成功,apply_fail:申请失败,refund_success:退款成功,refund_fail:退款失败)
	 */
	private String status;

	/**
	 * 通知地址(关闭/支付订单都会通知)
	 */
	@TableField(value = "notify_url")
	private String notifyUrl;

	/**
	 * 通知地址的附加数据
	 */
	@TableField(value = "notify_attach")
	private String notifyAttach;

	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private LocalDateTime updateTime;
}
