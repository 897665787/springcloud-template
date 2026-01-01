package com.company.user.wallet.impl;

import java.math.BigDecimal;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import com.company.framework.util.JsonUtil;
import com.company.user.api.enums.WalletEnum;
import com.company.user.service.WalletPreincomeService;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.Preincome;
import com.company.user.wallet.dto.WalletId;

/**
 * 待入账钱包
 *
 * <pre>
 * 使用场景：金额先到待入账记录起来，一段时间后或经过一定的规则校验后再讲金额写入到钱包余额
 * </pre>
 */
public class PreincomeWallet implements IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal>, Preincome {

	@Autowired
	private WalletPreincomeService walletPreincomeService;

	private String beanName;
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> wallet;

	public PreincomeWallet(IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> wallet) {
		this.wallet = wallet;
	}

	@Override
	public BigDecimal balance(WalletId walletId) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();

		BigDecimal sumPreAmount = walletPreincomeService.sumAmount(userId, walletType);
		return wallet.balance(walletId).add(sumPreAmount);
	}

	@Override
	public Integer income(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		Integer userId = walletId.getUserId();
		WalletEnum.Type walletType = walletId.getWalletType();
		walletPreincomeService.save(uniqueCode, userId, walletType, amount, this.beanName,
				JsonUtil.toJsonString(attachMap));
		return 0;
	}

	@Override
	public Integer outcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.outcome(uniqueCode, walletId, amount, attachMap);
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public Integer exeIncome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap) {
		return wallet.income(uniqueCode, walletId, amount, attachMap);
	}

}
