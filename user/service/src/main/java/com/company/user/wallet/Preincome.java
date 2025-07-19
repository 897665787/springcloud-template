package com.company.user.wallet;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;

import com.company.user.wallet.dto.WalletId;

/**
 * 待入账
 */
public interface Preincome extends BeanNameAware {

	/**
	 * 入账
	 * 
	 * @param uniqueCode
	 * @param walletId
	 * @param amount
	 * @param attachMap
	 * @return
	 */
	Integer exeIncome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap);
}
