package com.company.admin.entity.user.wallet;

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
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钱包历史
 * Created by JQ棣 on 2018/11/12.
 */
@Accessors(chain = true)
@Getter
@Setter
public class WalletHistory extends BaseModel {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "用户ID长度为1-32个字符",groups = {Save.class, Update.class})
	private String userId;

	/**
	 * 平台，1-iOS，2-Android
	 */
	@NotNull(message = "平台不能为空", groups = Save.class)
	@Range(min = 0, message = "平台至少为0", groups = {Save.class, Update.class})
	@AutoDesc({"1:iOS", "2:Android"})
	private Integer platform;

	/**
	 * 金额
	 */
	@NotNull(message = "金额不能为空", groups = Save.class)
	@DecimalMin(value = "0", message = "金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	private BigDecimal fee;

	/**
	 * 类型，1-增加，2-减少
	 */
	@NotNull(message = "类型不能为空", groups = Save.class)
	@Range(min = 0, message = "类型至少为0", groups = {Save.class, Update.class})
	@AutoDesc({"1:增加", "2:减少"})
	private Integer type;

	/**
	 * 事件类型，1-系统操作，2-用户充值，3-用户提现
	 */
	@NotNull(message = "事件类型不能为空", groups = Save.class)
	@Range(min = 0, message = "事件类型至少为0", groups = {Save.class, Update.class})
	@AutoDesc({"1:系统操作", "2:用户充值", "3:用户提现"})
	private Integer eventType;

	/**
	 * 事件ID
	 */
	@Length(max = 32, message = "事件ID长度最多为32个字符",groups = {Save.class, Update.class})
	private String eventId;

	/**
	 * 事件描述
	 */
	@NotBlank(message = "事件描述不能为空", groups = Save.class)
	@Length(min = 1, max = 256, message = "事件描述长度为1-256个字符",groups = {Save.class, Update.class})
	private String eventDesc;

	/**
	 * 完成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date completeTime;

	/**
	 * 变化之前的金额
	 */
	@NotNull(message = "变化之前的金额不能为空", groups = Save.class)
	@DecimalMin(value = "0", message = "变化之前的金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "变化之前的金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	private BigDecimal changeBefore;

	/**
	 * 变化之后的金额
	 */
	@NotNull(message = "变化之后的金额不能为空", groups = Save.class)
	@DecimalMin(value = "0", message = "变化之后的金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "变化之后的金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	private BigDecimal changeAfter;

	private String creatorId;

	private String creator;

	public interface Save {}

	public interface Update {}
}
