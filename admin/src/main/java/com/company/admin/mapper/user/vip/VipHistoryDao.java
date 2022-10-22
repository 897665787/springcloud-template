package com.company.admin.mapper.user.vip;

import com.company.admin.entity.user.User;
import com.company.admin.entity.user.vip.VipHistory;

import java.util.List;

/**
 * 钱包历史Dao
 * Created by xuxiaowei on 2018/11/12.
 */
public interface VipHistoryDao {

	int save(VipHistory vipHistory);

	List<VipHistory> list(User user);

	Long count(User user);
}
