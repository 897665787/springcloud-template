package com.company.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_ali_pay")
@Data
@Accessors(chain = true)
public class AliPay {
	/**
	 * ID
	 */
//	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * bu_user_info.id
	 */
	private Integer userId;

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
	 * 通知地址
	 */
	private String notifyUrl;
	
	/**
	 * 商户订单号，需保持唯一性
	 */
	private String outTradeNo;
	
	/**
	 * 商品标题
	 */
	private String subject;
	
	/**
	 * 订单金额(元)
	 */
	private BigDecimal totalAmount;

	/**
	 * 支付体（前端使用）
	 */
	private String payBody;

	/**
	 * 交易结果(TRADE_CLOSED:交易关闭,TRADE_FINISHED:交易完结,TRADE_SUCCESS:支付成功,WAIT_BUYER_PAY:交易创建)
	 */
	private String tradeStatus;
	
	/**
	 * 支付宝交易凭证号
	 */
	private String tradeNo;
	
	/**
	 * 交易支付时间
	 */
	private String gmtPayment;

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