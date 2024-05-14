package com.company.user.wallet.autoconfigure;

import java.math.BigDecimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.ExpirePartAmount;
import com.company.user.wallet.dto.FreezeAmount;
import com.company.user.wallet.dto.FreezeBalance;
import com.company.user.wallet.dto.FreezeMainChargeGiftAmount;
import com.company.user.wallet.dto.FreezeMainChargeGiftBalance;
import com.company.user.wallet.dto.IncomeAmount;
import com.company.user.wallet.dto.MainChargeGiftAmount;
import com.company.user.wallet.dto.MainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftWalletId;
import com.company.user.wallet.dto.OutcomeAmount;
import com.company.user.wallet.dto.WalletId;
import com.company.user.wallet.impl.BaseWallet;
import com.company.user.wallet.impl.ExpirePartWallet;
import com.company.user.wallet.impl.ExpireWallet;
import com.company.user.wallet.impl.FreezeMainChargeGiftWallet;
import com.company.user.wallet.impl.FreezeWallet;
import com.company.user.wallet.impl.MainChargeGiftWallet;
import com.company.user.wallet.impl.NormalWallet;
import com.company.user.wallet.impl.PreincomeWallet;

@Configuration
public class WalletAutoConfiguration {
	// 基础钱包
	@Bean
	public IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet() {
		return new BaseWallet();
	}

	// 普通钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> normalWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new NormalWallet(baseWallet);
	}

	// 待入账钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> preincomeWallet(
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> normalWallet) {
		return new PreincomeWallet(normalWallet);
	}

	// 过期部分未使用金额钱包
	@Bean
	public IWallet<WalletId, ExpirePartAmount, BigDecimal, BigDecimal> expirePartWallet(
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> normalWallet) {
		Integer expireMinutes = 7 * 24 * 60;
		return new ExpirePartWallet(normalWallet, expireMinutes);
	}

	// 过期未使用金额钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> expireWallet(
			IWallet<WalletId, ExpirePartAmount, BigDecimal, BigDecimal> expirePartWallet) {
		return new ExpireWallet(expirePartWallet);// 余额可过期钱包
	}

	// 待入账钱包-过期未使用金额
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> preincomeExpireWallet(
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> expireWallet) {
		return new PreincomeWallet(expireWallet);
	}

	// 冻结钱包
	@Bean
	public IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new FreezeWallet(baseWallet);
	}

	/****************************** 组合钱包 ******************************/
	// 主钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> mainWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new NormalWallet(baseWallet);
//		Integer expireMinutes = 7 * 24 * 60;
//		return new ExpirePartWallet(normalWallet, expireMinutes);// 部分余额可过期钱包
	}

	// 充值钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> chargeWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new NormalWallet(baseWallet);
	}

	// 赠送钱包
	@Bean
	public IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> giftWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new NormalWallet(baseWallet);
//		Integer expireMinutes = 7 * 24 * 60;
//		return new NormalExpireWallet(expireMinutes);// 余额可过期钱包
	}

	// 主钱包、充值钱包、赠送钱包组合
	@Bean
	public IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, MainChargeGiftAmount, MainChargeGiftBalance> mainChargeGiftWallet(
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> mainWallet,
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> chargeWallet,
			IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> giftWallet) {
		MainChargeGiftWallet mainChargeGiftWallet = new MainChargeGiftWallet(mainWallet, chargeWallet, giftWallet);
		return mainChargeGiftWallet;
	}

	/****************************** 冻结-组合钱包 ******************************/
	// 主钱包
	@Bean
	public IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeMainWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new FreezeWallet(baseWallet);
	}

	// 充值钱包
	@Bean
	public IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeChargeWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new FreezeWallet(baseWallet);
	}

	// 赠送钱包
	@Bean
	public IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeGiftWallet(
			IWallet<WalletId, IncomeAmount, OutcomeAmount, BigDecimal> baseWallet) {
		return new FreezeWallet(baseWallet);
	}

	// 主钱包、充值钱包、赠送钱包组合
	@Bean
	public IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, FreezeMainChargeGiftAmount, FreezeMainChargeGiftBalance> freezeMainChargeGiftWallet(
			IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeMainWallet,
			IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeChargeWallet,
			IWallet<WalletId, BigDecimal, FreezeAmount, FreezeBalance> freezeGiftWallet) {
		FreezeMainChargeGiftWallet freezeMainChargeGiftWallet = new FreezeMainChargeGiftWallet(freezeMainWallet,
				freezeChargeWallet, freezeGiftWallet);
		return freezeMainChargeGiftWallet;
	}
}