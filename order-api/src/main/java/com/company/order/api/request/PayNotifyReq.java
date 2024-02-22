package com.company.order.api.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayNotifyReq {
	public interface EVENT {
		String PAY = "pay";// 支付事件(默认)
		String CLOSE = "close";// 关闭事件
	}

	/**
	 * 事件(取值PayNotifyReq.EVENT)
	 */
	private String event;
	/**
	 * 成功
	 */
	private Boolean success;
	/**
	 * 信息
	 */
	private String message;
	
	/**
	 * 订单号<必填>
	 */
	@NotBlank(message = "订单号不能为空")
	private String orderCode;
	
	/**
	 * 支付/关闭时间<非必填>
	 */
	private LocalDateTime time;
	
	/**
	 * 附加参数<非必填>
	 * <p>
	 * 回调notifyUrl中原样返回，可作为自定义参数使用
	 */
	private String attach;
}
