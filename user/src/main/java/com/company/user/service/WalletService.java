package com.company.user.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.framework.lock.LockClient;
import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.Wallet;
import com.company.user.mapper.wallet.WalletMapper;

@Service
public class WalletService extends ServiceImpl<WalletMapper, Wallet> implements IService<Wallet> {

	@Autowired
	private LockClient lockClient;

	public Wallet getOrInit(Integer userId, WalletEnum.Type type) {
		Wallet wallet = baseMapper.selectByUserIdType(userId, type);
		if (wallet != null) {
			return wallet;
		}
		// 初始化钱包
		wallet = lockClient.doInLock(String.format("wallet:init:%s:%s", userId, type.getCode()), () -> {
			Wallet walletDB = baseMapper.selectByUserIdType(userId, type);
			if (walletDB == null) {
				Wallet initWallet = new Wallet().setUserId(userId).setType(type.getCode()).setBalance(BigDecimal.ZERO)
						.setStatus(WalletEnum.Status.NOMAL.getCode());
				baseMapper.insert(initWallet);
				walletDB = initWallet;
			}
			return walletDB;
		});
		return wallet;
	}

	public int income(Integer id, BigDecimal amount) {
		return baseMapper.income(id, amount);
	}

	public int outcome(Integer id, BigDecimal amount) {
		return baseMapper.outcome(id, amount);
	}
}
