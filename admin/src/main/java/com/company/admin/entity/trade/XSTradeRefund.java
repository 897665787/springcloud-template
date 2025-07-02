package com.company.admin.entity.trade;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;
import com.company.framework.jackson.annotation.FormatNumber;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 交易退款
 * Created by JQ棣 on 2018/06/28.
 */
@Accessors(chain = true)
@Getter
@Setter
public class XSTradeRefund extends BaseModel {

	/**
	 * 退款订单号
	 */
	@NotBlank(message = "退款订单号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "退款订单号长度为1-32个字符",groups = {Save.class, Update.class})
	private String id;

	/**
	 * 交易订单号，微信最多支持32位
	 */
	@NotBlank(message = "交易订单号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "交易订单号长度为1-32个字符",groups = {Save.class, Update.class})
	private String tradeId;

	/**
	 * 退款金额，单位元，支付宝最多支持精度(9,2)
	 */
	@NotNull(message = "退款金额不能为空", groups = Save.class)
	@DecimalMin(value = "0.01", message = "退款金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "退款金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	private BigDecimal fee;

	/**
	 * 状态，1-退款中，2-已退款
	 */
	@NotNull(message = "状态不能为空", groups = Save.class)
	@Range(min = 1, message = "状态至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:退款中", "2:已退款" })
	private Integer status;

	/**
	 * 失败原因
	 */
	@Length(max = 65535, message = "失败原因长度最多为65535个字符",groups = {Save.class, Update.class})
	private String failureReason;

	/**
	 * 第三方支付平台退款订单号，支付宝最多支持64位
	 */
	@Length(max = 64, message = "第三方支付平台退款订单号长度最多为64个字符",groups = {Save.class, Update.class})
	private String outId;

	public interface Save {}

	public interface Update {}

}
