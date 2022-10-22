package com.company.admin.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.CreditLog;
import com.company.admin.mapper.user.CreditLogDao;

/**
 * 积分记录Service
 * Created by JQ棣 on 2018/11/12.
 */
@Service
public class CreditLogService {

	@Autowired
	private CreditLogDao creditLogDao;

	public XSPageModel<CreditLog> listAndCount(CreditLog creditLog) {
		creditLog.setDefaultSort("id", "DESC");
		return XSPageModel.build(creditLogDao.list(creditLog), creditLogDao.count(creditLog));
	}
}
