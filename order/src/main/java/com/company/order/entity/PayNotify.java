package com.company.order.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_pay_notify")
@Data
@Accessors(chain = true)
public class PayNotify {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 支付方式(ali:支付宝,wx:微信,ios:苹果,quick:云闪付)
	 */
	private String method;

	/**
	 * 通知数据
	 */
	private String notifyData;

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