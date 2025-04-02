package com.company.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_financial_flow")
@Data
@Accessors(chain = true)
public class FinancialFlow implements Serializable {

	private static final long serialVersionUID = 1L;


	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 订单编号
	 */
	@TableField("order_code")
	private String orderCode;

	/**
	 * 渠道订单号
	 */
	@TableField("trade_no")
	private String tradeNo;

	/**
	 * 进出账金额
	 */
	@TableField("amount")
	private BigDecimal amount;

	/**
	 * 交易方式(wx_pay:微信支付,ali_pay:支付宝支付,mch_pay:微信企业打款)
	 */
	@TableField("trade_way")
	private String tradeWay;

	/**
	 * 业务来源 例优选套餐拉新助力：YX_HELP_BUY
	 */
	@TableField("business_source")
	private String businessSource;

	/**
	 * 商户号
	 */
	@TableField("merchant_no")
	private String merchantNo;

	/**
	 * 备注信息
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}