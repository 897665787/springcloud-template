package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.lock.LockClient;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletEnum.Type;
import com.company.user.entity.Wallet;
import com.company.user.entity.WalletFreeze;
import com.company.user.mapper.wallet.WalletFreezeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletFreezeService extends ServiceImpl<WalletFreezeMapper, WalletFreeze>
		implements IService<WalletFreeze> {

	@Autowired
	private WalletService walletService;

	@Autowired
	private LockClient lockClient;

	private static final String LOCK_NAME_WALLETCHANGE = "walletChange:%s";

	/**
	 * 冻结
	 * 
	 * @param uniqueCode
	 * @param walletId
	 * @param uniqueCode
	 * @param freezeAmount
	 */
	@Transactional
	public Integer freeze(String uniqueCode, String orderCode, Integer userId, WalletEnum.Type type,
			BigDecimal freezeAmount) {
		if (freezeAmount.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}
		WalletFreeze walletFreeze_1 = baseMapper.selectByUniqueCode(uniqueCode);
		if (walletFreeze_1 != null) {
			log.warn("uniqueCode:{},已出账", uniqueCode);
			return walletFreeze_1.getId();
		}

		Wallet wallet4id = walletService.getOrInit(userId, type);
		Integer walletId = wallet4id.getId();
		return lockClient.doInLock(String.format(LOCK_NAME_WALLETCHANGE, walletId), () -> {
			WalletFreeze walletFreeze_2 = baseMapper.selectByUniqueCode(uniqueCode);
			if (walletFreeze_2 != null) {
				log.warn("uniqueCode:{},已出账", uniqueCode);
				return walletFreeze_2.getId();
			}

			Wallet wallet = walletService.getById(walletId);
			
			// 钱支出扣减balance
			int affect = walletService.outcome(wallet.getId(), freezeAmount);
			if (affect == 0) {
				log.info("{}余额不足,balance:{},amount:{}", wallet.getId(), wallet.getBalance(), freezeAmount);
				return 0;
			}

			WalletFreeze record = new WalletFreeze();
			record.setUniqueCode(uniqueCode);
			record.setOrderCode(orderCode);
			record.setWalletId(wallet.getId());
			record.setFreezeAmount(freezeAmount);
			record.setStatus(1);
			baseMapper.insert(record);
			return 1;
		});
	}

	public Integer update2Use(String uniqueCode) {
		return baseMapper.update2Use(uniqueCode);
	}

	public Integer update2Return(String orderCode) {
		Integer affect = baseMapper.update2Return(orderCode);
		if (affect > 0) {
			List<WalletFreeze> walletFreezeList = baseMapper.selectByOrderCode(orderCode);
			for (WalletFreeze walletFreeze : walletFreezeList) {
				walletService.income(walletFreeze.getWalletId(), walletFreeze.getFreezeAmount());
			}
		}
		return affect;
	}

	public WalletFreeze selectByUniqueCode(String uniqueCode) {
		return baseMapper.selectByUniqueCode(uniqueCode);
	}

	public BigDecimal sumFreezeAmount(Integer userId, Type type) {
		Wallet wallet = walletService.getOrInit(userId, type);
		BigDecimal sumFreezeAmount = baseMapper.sumFreezeAmount(wallet.getId());
		return Optional.ofNullable(sumFreezeAmount).orElse(BigDecimal.ZERO);
	}

}
