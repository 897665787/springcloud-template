package com.company.admin.mapper.user.wallet;

import com.company.admin.entity.user.wallet.AliAccount;
import java.util.List;

/**
 * 支付宝Dao
 * Created by JQ棣 on 2018/11/21.
 */
public interface AliAccountDao {

	int save(AliAccount aliAccount);

	int remove(AliAccount aliAccount);

	int update(AliAccount aliAccount);

	AliAccount get(AliAccount aliAccount);

	List<AliAccount> list(AliAccount aliAccount);

	Long count(AliAccount aliAccount);
}
