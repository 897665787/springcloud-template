package com.company.admin.entity.trade;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 交易日志
 * Created by JQ棣 on 2018/06/29.
 */
@Accessors(chain = true)
@Getter
@Setter
public class XSTradeLog extends BaseModel {

	/**
	 * 主键，自动递增
	 */
	private Long id;

	/**
	 * 订单号，微信最多支持32位
	 */
	@NotBlank(message = "订单号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "订单号长度为1-32个字符",groups = {Save.class, Update.class})
	private String tradeId;

	/**
	 * 状态，1-已取消，2-待支付，3-已支付，4-退款中，5-已退款
	 */
	@NotNull(message = "状态不能为空", groups = Save.class)
	@Range(min = 1, message = "状态至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:已取消", "2:待支付", "3:已支付", "4:退款中", "5:已退款" })
	private Integer status;

	/**
	 * 操作者类型，1-用户，2-第三方支付平台，3-定时任务
	 */
	@NotNull(message = "操作者类型不能为空", groups = Save.class)
	@Range(min = 1, message = "操作者类型至少为1", groups = {Save.class, Update.class})
	@AutoDesc({ "1:用户", "2:第三方支付平台", "3:定时任务" })
	private Integer operatorType;

	/**
	 * 操作信息
	 */
	@Length(max = 65535, message = "操作信息长度最多为65535个字符",groups = {Save.class, Update.class})
	private String operationInfo;

	public interface Save {}

	public interface Update {}

}
