package com.company.admin.mapper.user.wallet;

import com.company.admin.entity.user.wallet.BankCard;
import java.util.List;

/**
 * 银行卡Dao
 * Created by JQ棣 on 2018/11/21.
 */
public interface BankCardDao {

	int save(BankCard bankCard);

	int remove(BankCard bankCard);

	int update(BankCard bankCard);

	BankCard get(BankCard bankCard);

	List<BankCard> list(BankCard bankCard);

	Long count(BankCard bankCard);
}
