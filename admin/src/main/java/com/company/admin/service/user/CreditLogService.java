package com.company.admin.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.CreditLog;
import com.company.admin.mapper.user.CreditLogDao;

/**
 * 积分记录Service
 * Created by JQ棣 on 2018/11/12.
 */
@Service
@RequiredArgsConstructor
public class CreditLogService {

	private final CreditLogDao creditLogDao;

	public XSPageModel<CreditLog> listAndCount(CreditLog creditLog) {
		creditLog.setDefaultSort("id", "DESC");
		return XSPageModel.build(creditLogDao.list(creditLog), creditLogDao.count(creditLog));
	}
}
