package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.WalletFreeze;
import com.company.user.service.WalletFreezeService;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.FreezeAmount;
import com.company.user.wallet.dto.FreezeBalance;
import com.company.user.wallet.dto.IncomeAmount;
import com.company.user.wallet.dto.OutcomeAmount;
import com.company.user.wallet.dto.WalletId;

/**
 * 冻结钱包
 * 
 * <pre>
 * 使用场景：在使用钱包余额支付，在余额不足的同时进行支付行为，需要先冻结现有的余额，但余额的出账不记录到WalletRecord，等后续支付回调后再统一记录，outcome方法会被调用2次
 * </pre>
 */
public class FreezeWallet implements IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> {

	@Autowired
	private WalletFreezeService walletFreezeService;

	private IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet;

	public FreezeWallet(IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		this.baseWallet = baseWallet;
	}

	@Override
	public FreezeBalance balance(WalletId walletId) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();

		BigDecimal sumFreezeAmount = walletFreezeService.sumFreezeAmount(userId, walletType);
		return new FreezeBalance(baseWallet.balance(walletId), sumFreezeAmount);
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		BigDecimal sumFreezeAmount = walletFreezeService.sumFreezeAmount(walletId.getUserId(),
				walletId.getWalletType());
		BigDecimal notInBlanceAmount = Optional.ofNullable(sumFreezeAmount).orElse(BigDecimal.ZERO);
		IncomeAmount incomeAmount = new IncomeAmount(amount, notInBlanceAmount);
		return baseWallet.income(uniqueCode, walletId, incomeAmount, attachMap);
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, FreezeAmount freezeAmount,
			Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();

		if (freezeAmount.getFreeze()) {
			return walletFreezeService.freeze(uniqueCode, freezeAmount.getOrderCode(), userId, walletType,
					freezeAmount.getAmount());
		} else {
			BigDecimal thisFreezeAmount = BigDecimal.ZERO;
			Integer affect = walletFreezeService.update2Use(uniqueCode);
			if (affect > 0) {
				WalletFreeze walletFreeze = walletFreezeService.selectByUniqueCode(uniqueCode);
				thisFreezeAmount = walletFreeze.getFreezeAmount();
			}
			BigDecimal totalOutAmount = freezeAmount.getAmount().add(thisFreezeAmount);// 加上之前的冻结金额

			OutcomeAmount outcomeAmount = new OutcomeAmount(freezeAmount.getAmount(), totalOutAmount);
			return baseWallet.outcome(uniqueCode, walletId, outcomeAmount, attachMap);
		}
	}
}
