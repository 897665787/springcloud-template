package com.company.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_ali_activity_notify")
@Data
@Accessors(chain = true)
public class AliActivityNotify {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 消息(spiordersend:订单券发放,from:FROM消息)
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
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}