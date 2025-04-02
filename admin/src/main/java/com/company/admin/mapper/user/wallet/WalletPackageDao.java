package com.company.admin.mapper.user.wallet;

import com.company.admin.entity.user.wallet.WalletPackage;
import java.util.List;

/**
 * 钱包套餐Dao
 * Created by JQ棣 on 2018/11/13.
 */
public interface WalletPackageDao {

	int save(WalletPackage walletPackage);

	int remove(WalletPackage walletPackage);

	int update(WalletPackage walletPackage);

	WalletPackage get(WalletPackage walletPackage);

	List<WalletPackage> list(WalletPackage walletPackage);

	Long count(WalletPackage walletPackage);
}
