package com.company.user.wallet.dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class IncomeAmount {
	BigDecimal amount;
	BigDecimal notInBlanceAmount;

	public IncomeAmount(BigDecimal amount) {
		this.amount = amount;
		this.notInBlanceAmount = BigDecimal.ZERO;
	}
}
