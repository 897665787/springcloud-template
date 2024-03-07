package com.company.order.api.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderFinishReq {
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 完成时间
	 */
	@NotNull(message = "完成时间不能为空")
	private LocalDateTime finishTime;
}
