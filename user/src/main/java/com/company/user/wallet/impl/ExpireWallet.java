package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.ExpirePartAmount;
import com.company.user.wallet.dto.WalletId;

/**
 * 过期未使用金额钱包
 * 
 * <pre>
 * 使用场景：入账金额有部分在指定时间内没有使用就会过期，如赠送的金额在一定时间内没有购买商品就过期掉
 * </pre>
 */
public class ExpireWallet implements IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> {

	private IWallet<WalletId, ExpirePartAmount, BigDecimal, BigDecimal> wallet;

	public ExpireWallet(IWallet<WalletId, ExpirePartAmount, BigDecimal, BigDecimal> wallet) {
		this.wallet = wallet;
	}

	@Override
	public BigDecimal balance(WalletId walletId) {
		return wallet.balance(walletId);
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.income(uniqueCode, walletId, new ExpirePartAmount(amount, amount), attachMap);
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.outcome(uniqueCode, walletId, amount, attachMap);
	}
}
