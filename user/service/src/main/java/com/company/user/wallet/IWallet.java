package com.company.user.wallet;

import java.util.Map;

/**
 * 钱包
 * 
 * @param <WI>
 *            钱包ID(唯一)
 * @param <IA>
 *            进账金额类型
 * @param <OA>
 *            出账金额类型
 * @param <B>
 *            余额类型
 */
public interface IWallet<WI, IA, OA, B> {
	/**
	 * 余额
	 *
	 * @param walletId
	 *            钱包ID(唯一)
	 * @return 余额
	 */
	B balance(WI walletId);

	/**
	 * 入账
	 * 
	 * @param uniqueCode
	 *            唯一码(用于幂等，防止重复入账)
	 * @param walletId
	 *            钱包ID(唯一)
	 * @param amount
	 *            金额
	 * @param attachMap
	 *            附加信息，透传
	 * @return 入账记录ID
	 */
	Integer income(String uniqueCode, WI walletId, IA amount, Map<String, Object> attachMap);
	
	/**
	 * 出账
	 *
	 * @param uniqueCode
	 *            唯一码(用于幂等，防止重复出账)
	 * @param walletId
	 *            钱包ID(唯一)
	 * @param amount
	 *            金额
	 * @param attachMap
	 *            附加信息，透传
	 * @return 出账成功：出账记录ID，出账失败：0
	 */
	Integer outcome(String uniqueCode, WI walletId, OA amount, Map<String, Object> attachMap);
}
