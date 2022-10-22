package com.company.admin.service.user.vip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.vip.VipPackage;
import com.company.admin.mapper.user.vip.VipPackageDao;
import com.company.common.exception.BusinessException;

/**
 * 会员套餐Service
 * Created by xuxiaowei on 2018/11/14.
 */
@Service
public class VipPackageService {

	@Autowired
	private VipPackageDao vipPackageDao;

	public void save(VipPackage vipPackage) {
		VipPackage existent = vipPackageDao.get(vipPackage);
		if (existent != null) {
			throw new BusinessException("Vip套餐已存在");
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
			throw new BusinessException("Vip套餐不存在");
		}
		return existent;
	}

	public XSPageModel<VipPackage> listAndCount(VipPackage vipPackage) {
		vipPackage.setDefaultSort("create_time", "DESC");
		return XSPageModel.build(vipPackageDao.list(vipPackage), vipPackageDao.count(vipPackage));
	}
}
