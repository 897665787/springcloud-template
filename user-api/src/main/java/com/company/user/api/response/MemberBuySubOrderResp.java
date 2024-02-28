package com.company.user.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemberBuySubOrderResp {
	/* 与前端约定添加子订单特有的字段 */
	/**
	 * 续期时间长(天)
	 */
	private Integer addDays;
	/* 与前端约定添加子订单特有的字段 */

	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
	/* 如有需要，使用相同的字段名覆盖OrderResp的字段值 */
}
