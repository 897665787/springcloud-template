package com.company.admin.mapper.user.vip;

import com.company.admin.entity.user.vip.VipPackage;
import java.util.List;

/**
 * 会员套餐Dao
 * Created by JQ棣 on 2018/11/14.
 */
public interface VipPackageDao {

	int save(VipPackage vipPackage);

	int remove(VipPackage vipPackage);

	int update(VipPackage vipPackage);

	VipPackage get(VipPackage vipPackage);

	List<VipPackage> list(VipPackage vipPackage);

	Long count(VipPackage vipPackage);
}
