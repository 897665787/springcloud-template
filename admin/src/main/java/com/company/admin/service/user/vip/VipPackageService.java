package com.company.admin.service.user.vip;

import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.vip.VipPackage;
import com.company.admin.mapper.user.vip.VipPackageDao;

/**
 * 会员套餐Service
 * Created by JQ棣 on 2018/11/14.
 */
@Service
@RequiredArgsConstructor
public class VipPackageService {

	private final VipPackageDao vipPackageDao;

	public void save(VipPackage vipPackage) {
		VipPackage existent = vipPackageDao.get(vipPackage);
		if (existent != null) {
			ExceptionUtil.throwException("Vip套餐已存在");
		}
		vipPackageDao.save(vipPackage);
	}

	public void remove(VipPackage vipPackage) {
		VipPackage existent = get(vipPackage);
		vipPackageDao.remove(existent);
	}

	public void update(VipPackage vipPackage) {
		VipPackage existent = get(vipPackage);
		vipPackageDao.update(vipPackage);
	}

	public VipPackage get(VipPackage vipPackage) {
		VipPackage existent = vipPackageDao.get(vipPackage);
		if (existent == null) {
			ExceptionUtil.throwException("Vip套餐不存在");
		}
		return existent;
	}

	public XSPageModel<VipPackage> listAndCount(VipPackage vipPackage) {
		vipPackage.setDefaultSort("create_time", "DESC");
		return XSPageModel.build(vipPackageDao.list(vipPackage), vipPackageDao.count(vipPackage));
	}
}
