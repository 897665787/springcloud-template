package com.company.user.wallet;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;

import com.company.user.wallet.dto.WalletId;

/**
 * 过期
 */
public interface Expire extends BeanNameAware {

	/**
	 * 过期出账
	 * 
	 * @param uniqueCode
	 * @param walletId
	 * @param amount
	 * @param attachMap
	 * @return
	 */
	Integer exeOutcome(String uniqueCode, WalletId walletId, BigDecimal amount, Map<String, Object> attachMap);
}
