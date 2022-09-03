package com.company.order.entity;

import java.math.BigDecimal;
import java.util.Date;

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
	 * 私钥
	 */
	private String privateKey;

	/**
	 * 公钥
	 */
	private String pubKey;

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
	 * 交易结果(TRADE_CLOSED:交易关闭,TRADE_FINISHED:交易完结,TRADE_SUCCESS:支付成功,WAIT_BUYER_PAY:交易创建)
	 */
	private String tradeStatus;
	
	/**
	 * 外部业务号(有值认为是退款)
	 */
	private String outBizNo;

	/**
	 * 关联bu_pay_notify ID
	 */
	private Integer payNotifyId;
	
	/**
	 * 备注信息
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
}