package com.company.order.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayOrderQueryResp {
	/**
	 * 有结果<必填>
	 */
	private Boolean result;

	/**
	 * 信息<result=false有值>
	 */
	private String message;
	
	/**
	 * 支付成功<result=true有值>
	 */
	private Boolean paySuccess;

	/**
	 * 支付金额<paySuccess=true有值>
	 */
	private BigDecimal payAmount;
	
	/**
	 * 支付时间<paySuccess=true有值>
	 */
	private LocalDateTime payTime;
	
	/**
	 * 商户号<paySuccess=true有值>
	 */
	private String merchantNo;
	
	/**
	 * 交易ID<paySuccess=true有值>
	 */
	private String tradeNo;
}
