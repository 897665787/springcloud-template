package com.company.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_wx_pay_refund")
@Data
@Accessors(chain = true)
public class WxPayRefund {
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
	 * 商户号
	 */
	private String mchid;

	/**
	 * 随机字符串
	 */
	private String nonceStr;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	
	/**
	 * 商户退款单号
	 */
	private String outRefundNo;
	/**
	 * 订单金额(分)
	 */
	private Integer totalFee;
	/**
	 * 订单金额(分)
	 */
	private Integer refundFee;

	/**
	 * 微信退款单号
	 */
	private String refundId;
	/**
	 * 现金支付金额(分)
	 */
	private Integer cashFee;
	/**
	 * 回调-退款状态(PROCESSING/SUCCESS/CHANGE/REFUNDCLOSE)
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