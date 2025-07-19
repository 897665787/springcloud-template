package com.company.user.wallet.dto;

import com.company.user.api.enums.WalletEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WalletId {
	Integer userId;
	WalletEnum.Type walletType;
}
