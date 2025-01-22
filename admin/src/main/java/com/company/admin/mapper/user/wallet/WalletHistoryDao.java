package com.company.admin.mapper.user.wallet;

import com.company.admin.entity.user.User;
import com.company.admin.entity.user.wallet.WalletHistory;

import java.util.List;

/**
 * 钱包历史Dao
 * Created by JQ棣 on 2018/11/12.
 */
public interface WalletHistoryDao {

	int save(WalletHistory walletHistory);

	List<WalletHistory> list(User user);

	Long count(User user);
}
