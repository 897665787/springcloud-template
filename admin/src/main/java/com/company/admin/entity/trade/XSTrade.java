package com.company.admin.entity.trade;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;
import com.company.framework.jackson.annotation.FormatNumber;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 交易
 * Created by JQ棣 on 2018/06/28.
 */
@Accessors(chain = true)
@Getter
@Setter
public class XSTrade extends BaseModel {

	/**
	 * 订单号，微信最多支持32位
	 */
	@NotBlank(message = "订单号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "订单号长度为1-32个字符",groups = {Save.class, Update.class})
	private String id;

	/**
	 * 用户号
	 */
	@NotBlank(message = "用户号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "用户号长度为1-32个字符",groups = {Save.class, Update.class})
	private String userId;

	/**
	 * 类型，1-会员，2-商品，3-充值，4-课程
	 */
	@NotNull(message = "类型不能为空", groups = Save.class)
	@Range(min = 1, message = "类型至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:会员", "2:商品", "3:充值", "4:课程" })
	private Integer type;

	/**
	 * 金额，单位元，支付宝最多支持精度(9,2)
	 */
	@NotNull(message = "金额不能为空", groups = Save.class)
	@DecimalMin(value = "0.01", message = "金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@FormatNumber(pattern = "0.##")
	private BigDecimal fee;

	/**
	 * 状态，1-已取消，2-待支付，3-已支付，4-退款中，5-已退款
	 */
	@NotNull(message = "状态不能为空", groups = Save.class)
	@Range(min = 1, message = "状态至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:已取消", "2:待支付", "3:已支付", "4:退款中", "5:已退款" })
	private Integer status;

	/**
	 * 渠道，1-支付宝，2-微信，3-iOS
	 */
	@NotNull(message = "渠道不能为空", groups = Save.class)
	@Range(min = 1, message = "渠道至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:支付宝", "2:微信", "3:iOS" })
	private Integer channel;

	/**
	 * 方式，11-支付宝APP支付，21-微信APP支付，22-微信JSAPI支付，31-iOS支付
	 * <br/>方式编号=渠道编号+该渠道内支付方式编号
	 */
	@NotNull(message = "方式不能为空", groups = Save.class)
	@Range(min = 1, message = "方式至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "11:支付宝APP支付", "21:微信APP支付", "22:微信JSAPI支付", "31:iOS支付" })
	private Integer mode;

	/**
	 * 平台，1-Android，2-iOS
	 */
	@NotNull(message = "平台不能为空", groups = Save.class)
	@Range(min = 1, message = "平台至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:Android", "2:iOS" })
	private Integer platform;

	/**
	 * 名称，微信最多支持128位
	 */
	@NotBlank(message = "名称不能为空", groups = Save.class)
	@Length(min = 1, max = 128, message = "名称长度为1-128个字符",groups = {Save.class, Update.class})
	private String name;

	/**
	 * 最晚支付时间
	 */
	@NotNull(message = "最晚支付时间不能为空", groups = Save.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expireTime;

	/**
	 * 退款状态，1-无退款，2-全额退款，3-部分退款
	 */
	@NotNull(message = "退款状态不能为空", groups = Save.class)
	@Range(min = 1, message = "退款状态至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:无退款", "2:全额退款", "3:部分退款" })
	private Integer refundStatus;

	/**
	 * 已退款金额，单位元，支付宝最多支持精度(9,2)
	 */
	@NotNull(message = "已退款金额不能为空", groups = Save.class)
	@DecimalMin(value = "0", message = "已退款金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "已退款金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@FormatNumber(pattern = "0.##")
	private BigDecimal refundFee;

	/**
	 * 失败原因
	 */
	@Length(max = 65535, message = "失败原因长度最多为65535个字符",groups = {Save.class, Update.class})
	private String failureReason;

	/**
	 * 第三方支付平台订单号，支付宝最多支持64位
	 */
	@Length(max = 64, message = "第三方支付平台订单号长度最多为64个字符",groups = {Save.class, Update.class})
	private String outId;

	/**
	 * 第三方支付平台用户号，微信最多支持128位
	 */
	@Length(max = 128, message = "第三方支付平台用户号长度最多为128个字符",groups = {Save.class, Update.class})
	private String outUserId;

	/**
	 * 退款订单号
	 */
	@Length(min = 1, max = 32, message = "退款订单号长度为1-32个字符",groups = {Save.class, Update.class})
	private String refundId;

	/**
	 * 第三方支付平台退款订单号，支付宝最多支持64位
	 */
	@Length(max = 64, message = "第三方支付平台退款订单号长度最多为64个字符",groups = {Save.class, Update.class})
	private String outRefundId;

	/**
	 * iOS支付票据，防止重复使用票据
	 */
	@Length(max = 15000, message = "iOS支付票据长度最多为15000个字符",groups = {Save.class, Update.class})
	private String iosReceipt;

	/**
	 * 真实数据，0-否，1-是
	 */
	@NotNull(message = "真实数据不能为空", groups = Save.class)
	@Range(min = 0, message = "真实数据至少为0", groups = {Save.class, Update.class})
	@AutoDesc({ "0:否", "1:是" })
	private Integer real;

	/**
	 * 回传参数，微信最多支持127位，第三方支付平台回调时将该参数原样返回
	 */
	private String passbackParams;

	/**
	 * 网页支付回跳地址
	 */
	private String returnUrl;

	/**
	 * 当前交易退款，仅在部分退款成功回调时有值
	 */
	private XSTradeRefund currentXSTradeRefund;

	public interface Save {}

	public interface Update {}

}
