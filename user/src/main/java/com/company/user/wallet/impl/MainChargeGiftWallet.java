package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.MainChargeGiftAmount;
import com.company.user.wallet.dto.MainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftWalletId;
import com.company.user.wallet.dto.WalletId;

/**
 * 组合钱包（由主钱包、充值钱包、赠送钱包组合）
 * 
 * <pre>
 * 使用场景：多个钱包当1个钱包用，可以实现业务上需要划分钱包扣款的功能。如总扣款15元，需要从充值钱包扣10元，赠送钱包扣5元
 * </pre>
 */
public class MainChargeGiftWallet
		implements IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, MainChargeGiftAmount, MainChargeGiftBalance> {
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> mainWallet;
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> chargeWallet;
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> giftWallet;

	public MainChargeGiftWallet(IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> mainWallet,
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> chargeWallet,
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> giftWallet) {
		this.mainWallet = mainWallet;
		this.chargeWallet = chargeWallet;
		this.giftWallet = giftWallet;
	}

	@Override
	public MainChargeGiftBalance balance(MainChargeGiftWalletId walletId) {
		Integer userId = walletId.getUserId();
		MainChargeGiftBalance balance = new MainChargeGiftBalance(
				mainWallet.balance(new WalletId(userId, walletId.getMainType())),
				chargeWallet.balance(new WalletId(userId, walletId.getChargeType())),
				giftWallet.balance(new WalletId(userId, walletId.getGiftType())));
		return balance;
	}

	@Transactional
	@Override
	public Integer income(String uniqueCode, MainChargeGiftWalletId walletId, MainChargeGiftAmount amount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		chargeWallet.income("charge-" + uniqueCode, new WalletId(userId, walletId.getChargeType()),
				amount.getChargeAmount(), attachMap);
		giftWallet.income("gift-" + uniqueCode, new WalletId(userId, walletId.getGiftType()), amount.getGiftAmount(),
				attachMap);
		return mainWallet.income("main-" + uniqueCode, new WalletId(userId, walletId.getMainType()),
				amount.getMainAmount(), attachMap);
	}

	@Transactional
	@Override
	public Integer outcome(String uniqueCode, MainChargeGiftWalletId walletId, MainChargeGiftAmount amount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		chargeWallet.outcome("charge-" + uniqueCode, new WalletId(userId, walletId.getChargeType()),
				amount.getChargeAmount(), attachMap);
		giftWallet.outcome("gift-" + uniqueCode, new WalletId(userId, walletId.getGiftType()), amount.getGiftAmount(),
				attachMap);
		return mainWallet.outcome("main-" + uniqueCode, new WalletId(userId, walletId.getMainType()),
				amount.getMainAmount(), attachMap);
	}
}
