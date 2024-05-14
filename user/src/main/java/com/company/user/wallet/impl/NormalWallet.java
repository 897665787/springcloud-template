package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.IncomeAmount;
import com.company.user.wallet.dto.OutcomeAmount;
import com.company.user.wallet.dto.WalletId;

/**
 * 普通钱包
 * 
 * <pre>
 * 使用场景：最简单的钱包出入账
 * </pre>
 */
public class NormalWallet implements IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> {

	private IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> wallet;

	public NormalWallet(IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> wallet) {
		this.wallet = wallet;
	}

	@Override
	public BigDecimal balance(WalletId walletId) {
		return wallet.balance(walletId);
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.income(uniqueCode, walletId, new IncomeAmount(amount), attachMap);
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.outcome(uniqueCode, walletId, new OutcomeAmount(amount), attachMap);
	}
}
