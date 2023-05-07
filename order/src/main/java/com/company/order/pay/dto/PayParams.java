package com.company.order.pay.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 支付参数
 */
@Data
public class PayParams {
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 订单金额(元)
	 */
	private BigDecimal amount;

	/**
	 * 应用ID
	 */
	private String appid;
	
	/**
	 * <pre>
	 * 字段名：商品描述.
	 * 变量名：body
	 * 是否必填：是
	 * 类型：String(128)
	 * 示例值： 腾讯充值中心-QQ会员充值
	 * 描述：商品简单描述，该字段须严格按照规范传递，具体请见参数规定
	 * </pre>
	 */
	private String body;

	/**
	 * <pre>
	 * 字段名：商户订单号.
	 * 变量名：out_trade_no
	 * 是否必填：是
	 * 类型：String(32)
	 * 示例值：20150806125346
	 * 描述：商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	 * </pre>
	 */
	private String outTradeNo;
	
	/**
	 * <pre>
	 * 字段名：终端IP.
	 * 变量名：spbill_create_ip
	 * 是否必填：是
	 * 类型：String(16)
	 * 示例值：123.12.12.123
	 * 描述：APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	 * </pre>
	 */
	private String spbillCreateIp;
	
	/**
	 * <pre>
	 * 字段名：商品Id.
	 * 变量名：product_id
	 * 是否必填：否
	 * 类型：String(32)
	 * 示例值：12235413214070356458058
	 * 描述：trade_type=NATIVE，此参数必传。此id为二维码中包含的商品Id，商户自行定义。
	 * </pre>
	 */
	private String productId;
	
	/**
	 * <pre>
	 * 字段名：用户标识.
	 * 变量名：openid
	 * 是否必填：否
	 * 类型：String(128)
	 * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
	 * 描述：trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
	 * openid如何获取，可参考【获取openid】。
	 * 企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
	 * </pre>
	 */
	private String openid;
}
