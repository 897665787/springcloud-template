package com.company.admin.entity.user.wallet;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

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
 * 提现
 * Created by JQ棣 on 2018/11/23.
 */
@Accessors(chain = true)
@Getter
@Setter
public class Withdrawal extends BaseModel {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 事件类型
	 */
	@NotNull(message = "事件类型不能为空", groups = Save.class)
	@Range(min = 1, message = "事件类型至少为1", groups = {Save.class, Update.class})
	private Integer eventType;

	/**
	 * 钱包类型，1-iOS，2-Android
	 */
	@NotNull(message = "钱包类型不能为空", groups = Save.class)
	@Range(min = 1, message = "钱包类型至少为1", groups = {Save.class, Update.class})
	@AutoDesc({"1:iOS", "2:Android"})
	private Integer walletType;

	/**
	 * 金额
	 */
	@NotNull(message = "金额不能为空", groups = Save.class)
	@DecimalMin(value = "0.01", message = "金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "金额范围为0.01-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@FormatNumber(pattern = "0.##")
	private BigDecimal fee;

	/**
	 * 状态，1-未审核，2-已通过，3-已拒绝
	 */
	@AutoDesc({"1:未审核", "2:已通过", "3:已拒绝"})
	private Integer status;

	/**
	 * 提现渠道，1-银行卡，2-支付宝，3-微信打款
	 */
	@NotNull(message = "提现渠道不能为空", groups = Save.class)
	@Range(min = 1, message = "提现渠道至少为1", groups = {Save.class, Update.class})
	@AutoDesc({"1:银行卡", "2:支付宝", "3:微信打款"})
	private Integer channel;

	/**
	 * 申请时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date applicationTime;

	/**
	 * 审核人
	 */
	private String auditorId;

	/**
	 * 审核人名称
	 */
	private String auditorName;

	/**
	 * 通过时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date passTime;

	/**
	 * 拒绝原因
	 */
	private String rejectReason;

	public interface Save {}

	public interface Update {}
}
