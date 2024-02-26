package com.company.tool.api.response;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubOrderDemo3SubOrderResp {
	/* 与前端约定添加子订单特有的字段 */
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
	/**
	 * 状态文案
	 */
	private String statusText;

	/**
	 * 付款文案
	 */
	private String payText;

	/**
	 * 时间文案
	 */
	private String timeText;

	/**
	 * 时间
	 */
	private Date time;
	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
}
