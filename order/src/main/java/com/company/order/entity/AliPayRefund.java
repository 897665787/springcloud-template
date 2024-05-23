package com.company.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_ali_pay_refund")
@Data
@Accessors(chain = true)
public class AliPayRefund {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 申请商户号的appid或商户号绑定的appid
	 */
	private String appid;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	
	/**
	 * 商户退款单号,部分退款必填
	 */
	private String outRequestNo;
	
	/**
	 * 退款金额(元)
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 支付宝交易凭证号
	 */
	private String tradeNo;
	
	/**
	 * 退款状态(REFUND_SUCCESS:退款成功)
	 */
	private String refundStatus;
	
	/**
	 * 备注信息
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}