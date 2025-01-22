package com.company.admin.mapper.user.wallet;

import com.company.admin.entity.user.wallet.Withdrawal;
import java.util.List;

/**
 * 提现Dao
 * Created by JQ棣 on 2018/11/23.
 */
public interface WithdrawalDao {

	int remove(Withdrawal withdrawal);

	int update(Withdrawal withdrawal);

	Withdrawal get(Withdrawal withdrawal);

	List<Withdrawal> list(Withdrawal withdrawal);

	Long count(Withdrawal withdrawal);
}
