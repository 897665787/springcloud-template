package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.FreezeAmount;
import com.company.user.wallet.dto.FreezeBalance;
import com.company.user.wallet.dto.FreezeMainChargeGiftAmount;
import com.company.user.wallet.dto.FreezeMainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftAmount;
import com.company.user.wallet.dto.MainChargeGiftWalletId;
import com.company.user.wallet.dto.WalletId;

/**
 * 冻结钱包-组合钱包（由主钱包、充值钱包、赠送钱包组合）
 * 
 * <pre>
 * 使用场景：多个钱包当1个钱包用，可以实现业务上需要划分钱包扣款的功能。如总扣款15元，需要从充值钱包扣10元，赠送钱包扣5元
 * </pre>
 */
public class FreezeMainChargeGiftWallet implements
		IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, FreezeMainChargeGiftAmount, FreezeMainChargeGiftBalance> {
	private IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> mainWallet;
	private IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> chargeWallet;
	private IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> giftWallet;

	public FreezeMainChargeGiftWallet(IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> mainWallet,
			IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> chargeWallet,
			IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> giftWallet) {
		this.mainWallet = mainWallet;
		this.chargeWallet = chargeWallet;
		this.giftWallet = giftWallet;
	}

	@Override
	public FreezeMainChargeGiftBalance balance(MainChargeGiftWalletId walletId) {
		Integer userId = walletId.getUserId();
		FreezeMainChargeGiftBalance balance = new FreezeMainChargeGiftBalance(
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
	public Integer outcome(String uniqueCode, MainChargeGiftWalletId walletId, FreezeMainChargeGiftAmount amount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		chargeWallet.outcome("charge-" + uniqueCode, new WalletId(userId, walletId.getChargeType()),
				new FreezeAmount(uniqueCode, amount.getChargeAmount(), amount.getFreeze()), attachMap);
		giftWallet.outcome("gift-" + uniqueCode, new WalletId(userId, walletId.getGiftType()),
				new FreezeAmount(uniqueCode, amount.getGiftAmount(), amount.getFreeze()), attachMap);
		return mainWallet.outcome("main-" + uniqueCode, new WalletId(userId, walletId.getMainType()),
				new FreezeAmount(uniqueCode, amount.getMainAmount(), amount.getFreeze()), attachMap);
	}
}
