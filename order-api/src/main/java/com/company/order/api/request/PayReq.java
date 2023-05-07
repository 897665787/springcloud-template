package com.company.order.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.order.api.enums.OrderPayEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayReq {
	
	/**
	 * 用户ID<必填>
	 */
	@NotNull(message = "用户ID不能为空")
	private Integer userId;

	/**
	 * 订单号<必填>
	 */
	@NotBlank(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 业务类型(nomal:普通下单,kill:秒杀下单,member:购买会员)<必填>
	 */
	@NotNull(message = "业务类型不能为空")
	private OrderPayEnum.BusinessType businessType;

	/**
	 * 支付方式，1：支付宝；2：微信；3：银行卡<必填>
	 */
	@NotNull(message = "支付方式不能为空")
	private OrderPayEnum.Method method;

	/**
	 * 支付应用ID<必填>
	 */
	@NotNull(message = "支付应用ID不能为空")
	private String appid;

	/**
	 * 金额(元)<必填>
	 */
	@NotNull(message = "金额不能为空")
	private BigDecimal amount;
	
	/**
	 * 商品描述<必填>
	 */
	@NotBlank(message = "商品描述不能为空")
	private String body;
	
	/**
	 * 终端IP<必填>
	 */
	@NotBlank(message = "终端IP不能为空")
	private String spbillCreateIp;
	
	/**
	 * 商品Id<非必填>
	 */
	private String productId;

	/**
	 * 用户标识<非必填>
	 */
	private String openid;
	
	/* ======================内部服务====================== */
	/**
	 * 回调url（微服务内部回调）<非必填>
	 * 
	 * <p>
	 * 例：
	 * <p>
	 * url：http://template-user/goods/buyNotify
	 * <p>
	 * 请求方式：POST(强制)
	 * <p>
	 * 参数：com.company.order.api.request.PayNotifyReq
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
	 * <pre>
	 * X秒后未支付订单则关闭订单
	 * 订单超时后会回调notifyUrl
	 * (为null则使用默认超时)
	 * </pre>
	 */
	private Integer timeoutSeconds;
	
	private String remark;
}
