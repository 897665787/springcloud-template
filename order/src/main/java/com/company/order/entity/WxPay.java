package com.company.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_wx_pay")
@Data
@Accessors(chain = true)
public class WxPay {
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
	 * 商品描述
	 */
	private String body;
	/**
	 * 商户订单号，需保持唯一性
	 */
	private String outTradeNo;
	/**
	 * 标价金额(分)
	 */
	private Integer totalFee;
	/**
	 * 终端IP
	 */
	private String spbillCreateIp;
	/**
	 * 通知地址
	 */
	private String notifyUrl;
	/**
	 * 小程序:JSAPI,H5:MWEB,APP:APP
	 */
	private String tradeType;
	/**
	 * 商品ID(trade_type=NATIVE，此参数必传)
	 */
	private String productId;
	/**
	 * 用户标识(trade_type=JSAPI,此参数必传)
	 */
	private String openid;

	/**
	 * 返回状态码(SUCCESS/FAIL)
	 */
	private String returnCode;

	/**
	 * 返回状态信息
	 */
	private String returnMsg;

	/**
	 * 业务结果(SUCCESS/FAIL)
	 */
	private String resultCode;

	/**
	 * 错误代码
	 */
	private String errCode;

	/**
	 * 错误代码描述
	 */
	private String errCodeDes;
	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;
	/**
	 * 支付跳转链接
	 */
	private String mwebUrl;
	/**
	 * 二维码链接
	 */
	private String codeUrl;

	/**
	 * 回调-业务结果(SUCCESS/FAIL)
	 */
	private String notifyResultCode;

	/**
	 * 回调-错误代码
	 */
	private String notifyErrCode;

	/**
	 * 回调-错误代码描述
	 */
	private String notifyErrCodeDes;
	
	/**
	 * 微信支付订单号
	 */
	private String transactionId;
	/**
	 * 支付完成时间(yyyyMMddHHmmss)
	 */
	private String timeEnd;
	
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
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}