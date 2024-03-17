package com.company.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("bu_order_pay")
public class OrderPay {

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
	 * 订单编号
	 */
	@TableField("order_code")
	private String orderCode;
	/**
	 * 业务类型(nomal:普通下单,kill:秒杀下单,member:购买会员)
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
	 * 商品描述
	 */
	private String body;
	
	/**
	 * 商品Id
	 */
	private String productId;
	
	/**
	 * 状态(waitpay:待支付,closed:已关闭,payed:已支付)
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
	 * 关闭/支付时间
	 */
	@TableField(value = "pay_time")
	private LocalDateTime payTime;

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
