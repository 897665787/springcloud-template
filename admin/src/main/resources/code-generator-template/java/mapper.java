package com.company.admin.mapper.{module};

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.admin.entity.{module}.{ModelName};

public interface {ModelName}Dao extends BaseMapper<{ModelName}> {

	List<{ModelName}> list({ModelName} orderPayRefund);
	
	Long count({ModelName} orderPayRefund);

}