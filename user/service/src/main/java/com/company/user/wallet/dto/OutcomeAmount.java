package com.company.user.wallet.dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OutcomeAmount {
	BigDecimal thisOutAmount;
	BigDecimal totalOutAmount;

	public OutcomeAmount(BigDecimal thisOutAmount) {
		this.thisOutAmount = thisOutAmount;
		this.totalOutAmount = thisOutAmount;
	}
}
