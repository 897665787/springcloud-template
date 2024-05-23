package com.company.order.api.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayRefundQueryResp {
	/**
	 * 有结果<必填>
	 */
	private Boolean result;

	/**
	 * 信息<result=false有值>
	 */
	private String message;
	
	/**
	 * 退款成功<result=true有值>
	 */
	private Boolean refundSuccess;

	/**
	 * 退款金额<refundSuccess=true有值>
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 商户号<refundSuccess=true有值>
	 */
	private String merchantNo;
	
	/**
	 * 交易ID<refundSuccess=true有值>
	 */
	private String tradeNo;
}
