package com.company.user.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WalletRecordResp {
	/**
	 * 金额(元)<正数>
	 */
	private BigDecimal amount;
	/**
	 * 类型(1:入账,2:出账)
	 */
	private Integer type;
	/**
	 * 变化前的余额(元)
	 */
	private BigDecimal balanceBefore;
	/**
	 * 变化后的余额(元)
	 */
	private BigDecimal balanceAfter;

	private LocalDateTime createTime;
}