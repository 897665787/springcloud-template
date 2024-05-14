package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.user.service.WalletIncomeUseRecordService;
import com.company.user.wallet.Expire;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.ExpirePartAmount;
import com.company.user.wallet.dto.WalletId;

/**
 * 过期部分未使用金额钱包
 * 
 * <pre>
 * 使用场景：入账金额有部分在指定时间内没有使用就会过期，如充值的同时赠送一定的金额，赠送金额会过期，但是充值金额不会过期
 * </pre>
 */
public class ExpirePartWallet implements IWallet<WalletId, ExpirePartAmount, BigDecimal, BigDecimal>, Expire {

	@Autowired
	private WalletIncomeUseRecordService walletIncomeUseRecordService;

	private String beanName;
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> wallet;
	private Integer expireMinutes;

	public ExpirePartWallet(IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> wallet, Integer expireMinutes) {
		this.wallet = wallet;
		this.expireMinutes = expireMinutes;
	}

	@Override
	public BigDecimal balance(WalletId walletId) {
		return wallet.balance(walletId);
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, ExpirePartAmount amount,
			Map<String, Object> attachMap) {
		Integer walletRecordId = wallet.income(uniqueCode, walletId, amount.getTotalAmount(), attachMap);
		walletIncomeUseRecordService.save(walletRecordId, amount.getExpireAmount(), expireMinutes, this.beanName);
		return walletRecordId;
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		Integer walletRecordId = wallet.outcome(uniqueCode, walletId, amount, attachMap);
		if (walletRecordId != 0) {
			walletIncomeUseRecordService.use(uniqueCode, walletId.getUserId(), walletId.getWalletType(), amount);
		}
		return walletRecordId;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public Integer exeOutcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.income(uniqueCode, walletId, amount, attachMap);
	}
}
