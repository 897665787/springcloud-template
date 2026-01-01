package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import com.company.framework.util.JsonUtil;
import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.Wallet;
import com.company.user.service.WalletRecordService;
import com.company.user.service.WalletService;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.IncomeAmount;
import com.company.user.wallet.dto.OutcomeAmount;
import com.company.user.wallet.dto.WalletId;

/**
 * 基础钱包
 *
 * <pre>
 * 使用场景：出入账修改钱包余额Wallet.balance和新增WalletRecord.amount不一致时使用，比如冻结业务钱包余额分2次出账
 * </pre>
 */
public class BaseWallet implements IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> {

	@Autowired
	private WalletService walletService;

	@Autowired
	private WalletRecordService walletRecordService;

	@Override
	public BigDecimal balance(WalletId walletId) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();
		Wallet wallet = walletService.getOrInit(userId, walletType);
		return wallet.getBalance();
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, IncomeAmount incomeAmount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();
		return walletRecordService.income(uniqueCode, userId, walletType, incomeAmount.getAmount(),
				incomeAmount.getNotInBlanceAmount(), JsonUtil.toJsonString(attachMap));
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, OutcomeAmount outcomeAmount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();
		return walletRecordService.outcome(uniqueCode, userId, walletType, outcomeAmount.getThisOutAmount(),
				outcomeAmount.getTotalOutAmount(), JsonUtil.toJsonString(attachMap));
	}

}
