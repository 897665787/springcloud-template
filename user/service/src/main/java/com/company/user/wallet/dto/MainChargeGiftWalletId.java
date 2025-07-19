package com.company.user.wallet.dto;

import com.company.user.api.enums.WalletEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MainChargeGiftWalletId {
	Integer userId;
	WalletEnum.Type mainType;
	WalletEnum.Type chargeType;
	WalletEnum.Type giftType;
}
