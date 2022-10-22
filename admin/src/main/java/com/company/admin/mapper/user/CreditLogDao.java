package com.company.admin.mapper.user;

import com.company.admin.entity.user.CreditLog;
import java.util.List;

/**
 * 积分记录Dao
 * Created by JQ棣 on 2018/11/12.
 */
public interface CreditLogDao {

	List<CreditLog> list(CreditLog creditLog);

	Long count(CreditLog creditLog);
}
