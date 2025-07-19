package com.company.user.wallet.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FreezeMainChargeGiftBalance {
	FreezeBalance mainBalance;
	FreezeBalance chargeBalance;
	FreezeBalance giftBalance;
}
