package com.company.user.api.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CalcCanRefundAmountResp {
	private BigDecimal canRefundAmount;
	private String attach;
}
