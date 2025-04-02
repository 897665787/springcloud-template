package com.company.admin.service.user.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.wallet.WalletPackage;
import com.company.admin.mapper.user.wallet.WalletPackageDao;
import com.company.common.exception.BusinessException;

/**
 * 钱包套餐Service
 * Created by JQ棣 on 2018/11/13.
 */
@Service
public class WalletPackageService {

	@Autowired
	private WalletPackageDao walletPackageDao;

	public void save(WalletPackage walletPackage) {
		WalletPackage existedPackage = walletPackageDao.get(walletPackage);
		if (existedPackage != null) {
			throw new BusinessException("钱包套餐已存在");
		}
		walletPackageDao.save(walletPackage);
	}

	public void remove(WalletPackage walletPackage) {
		WalletPackage existent = get(walletPackage);
		walletPackageDao.remove(existent);
	}

	public void update(WalletPackage walletPackage) {
		WalletPackage existent = get(walletPackage);
		walletPackageDao.update(walletPackage);
	}

	public WalletPackage get(WalletPackage walletPackage) {
		WalletPackage existent = walletPackageDao.get(walletPackage);
		if (existent == null) {
			throw new BusinessException("钱包套餐不存在");
		}
		return existent;
	}

	public XSPageModel<WalletPackage> listAndCount(WalletPackage walletPackage) {
		walletPackage.setDefaultSort("id", "DESC");
		return XSPageModel.build(walletPackageDao.list(walletPackage), walletPackageDao.count(walletPackage));
	}
}
