package com.company.tool.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubOrderDemo3SubOrderDetailResp {
	/* 与前端约定添加子订单特有的字段 */
	
	/**
	 * 用户备注
	 */
	private String userRemark;
	
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderDetailResp的字段值 */
	/**
	 * 状态文案
	 */
	private String statusText;

	/**
	 * 付款文案
	 */
	private String payText;
	/* 如有需要，使用相同的字段名覆盖OrderDetailResp的字段值 */
}
