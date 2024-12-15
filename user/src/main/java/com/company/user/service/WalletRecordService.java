package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.lock.LockClient;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletRecordEnum;
import com.company.user.entity.Wallet;
import com.company.user.entity.WalletRecord;
import com.company.user.mapper.wallet.WalletRecordMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletRecordService extends ServiceImpl<WalletRecordMapper, WalletRecord>
		implements IService<WalletRecord> {

	@Autowired
	private WalletService walletService;

	@Autowired
	private LockClient lockClient;

	private static final String LOCK_NAME_WALLETCHANGE = "walletChange:%s";

	/**
	 * 入账
	 * 
	 * @param uniqueCode
	 * @param walletId
	 * @param amount
	 * @param businessType
	 * @param businessId
	 */
	@Transactional
	public Integer income(String uniqueCode, Integer userId, WalletEnum.Type type, BigDecimal amount,
			BigDecimal notInBlanceAmount, String attach) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}
		WalletRecord walletRecord_1 = baseMapper.selectByUniqueCodeType(uniqueCode, WalletRecordEnum.Type.IN);
		if (walletRecord_1 != null) {
			log.warn("uniqueCode:{},已入账", uniqueCode);
			return walletRecord_1.getId();
		}

		Wallet wallet4id = walletService.getOrInit(userId, type);
		Integer walletId = wallet4id.getId();
		return lockClient.doInLock(String.format(LOCK_NAME_WALLETCHANGE, walletId), () -> {
			WalletRecord walletRecord_2 = baseMapper.selectByUniqueCodeType(uniqueCode, WalletRecordEnum.Type.IN);
			if (walletRecord_2 != null) {
				log.warn("uniqueCode:{},已入账", uniqueCode);
				return walletRecord_2.getId();
			}

			Wallet wallet = walletService.getById(walletId);

			WalletRecord walletRecord = new WalletRecord();
			walletRecord.setUniqueCode(uniqueCode);
			walletRecord.setWalletId(wallet.getId());
			walletRecord.setAmount(amount);
			walletRecord.setType(WalletRecordEnum.Type.IN.getCode());

			BigDecimal balanceBefore = wallet.getBalance().add(notInBlanceAmount);
			walletRecord.setBalanceBefore(balanceBefore);

			BigDecimal balanceAfter = balanceBefore.add(amount);
			walletRecord.setBalanceAfter(balanceAfter);

			walletRecord.setAttach(attach);
			baseMapper.insert(walletRecord);

			// 钱入账到balance
			walletService.income(wallet.getId(), amount);
			return walletRecord.getId();
		});
	}

	/**
	 * 出账
	 *
	 * @param uniqueCode
	 * @param walletId
	 * @param amount
	 * @param walletRecordAmount
	 * @param businessType
	 * @param businessId
	 * @return 出账是否成功
	 */
	@Transactional
	public Integer outcome(String uniqueCode, Integer userId, WalletEnum.Type type, BigDecimal amount,
			BigDecimal totalOutAmount, String attach) {
		if (totalOutAmount.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}

		WalletRecord walletRecord_1 = baseMapper.selectByUniqueCodeType(uniqueCode, WalletRecordEnum.Type.OUT);
		if (walletRecord_1 != null) {
			log.warn("uniqueCode:{},已出账", uniqueCode);
			return walletRecord_1.getId();
		}

		Wallet wallet4id = walletService.getOrInit(userId, type);
		Integer walletId = wallet4id.getId();
		return lockClient.doInLock(String.format(LOCK_NAME_WALLETCHANGE, walletId), () -> {
			WalletRecord walletRecord_2 = baseMapper.selectByUniqueCodeType(uniqueCode, WalletRecordEnum.Type.OUT);
			if (walletRecord_2 != null) {
				log.warn("uniqueCode:{},已出账", uniqueCode);
				return walletRecord_2.getId();
			}

			Wallet wallet = walletService.getById(walletId);
			// 钱支出扣减balance
			int affect = walletService.outcome(wallet.getId(), amount);
			if (affect == 0) {
				log.info("{}余额不足,balance:{},amount:{}", wallet.getId(), wallet.getBalance(), amount);
				return 0;
			}

			WalletRecord walletRecord = new WalletRecord();
			walletRecord.setUniqueCode(uniqueCode);
			walletRecord.setWalletId(wallet.getId());

			walletRecord.setAmount(totalOutAmount);
			walletRecord.setType(WalletRecordEnum.Type.OUT.getCode());

			BigDecimal alreadyOutAmount = totalOutAmount.subtract(amount);
			BigDecimal balanceBefore = wallet.getBalance().add(alreadyOutAmount);
			walletRecord.setBalanceBefore(balanceBefore);

			BigDecimal balanceAfter = balanceBefore.subtract(totalOutAmount);
			walletRecord.setBalanceAfter(balanceAfter);

			walletRecord.setAttach(attach);
			baseMapper.insert(walletRecord);
			return walletRecord.getId();
		});
	}

	/**
	 * 分页查询钱包流水
	 */
	public List<WalletRecord> page(Integer walletId, Integer current, Integer size) {
		Page<WalletRecord> page = new Page<>(current, size);
		return baseMapper.pageByWalletId(page, walletId);
	}
}
