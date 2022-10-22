package com.company.admin.service.user.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.wallet.AliAccount;
import com.company.admin.mapper.user.wallet.AliAccountDao;
import com.company.common.exception.BusinessException;

/**
 * 支付宝Service
 * Created by xuxiaowei on 2018/11/21.
 */
@Service
public class AliAccountService {

	@Autowired
	private AliAccountDao aliAccountDao;

	public void save(AliAccount aliAccount) {
		aliAccountDao.save(aliAccount);
	}

	public void remove(AliAccount aliAccount) {
		AliAccount existent = get(aliAccount);
		aliAccountDao.remove(existent);
	}

	public void update(AliAccount aliAccount) {
		AliAccount existent = get(aliAccount);
		aliAccountDao.update(aliAccount);
	}

	public AliAccount get(AliAccount aliAccount) {
		AliAccount existent = aliAccountDao.get(aliAccount);
		if (existent == null) {
			throw new BusinessException("支付宝账号不存在");
		}
		return existent;
	}

	public XSPageModel<AliAccount> listAndCount(AliAccount aliAccount) {
		return XSPageModel.build(aliAccountDao.list(aliAccount), aliAccountDao.count(aliAccount));
	}
}
