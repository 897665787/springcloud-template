package com.company.admin.mapper.interact;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.company.admin.entity.interact.CollectNum;

/**
 * 收藏数Dao
 * Created by JQ棣 on 2018/11/08.
 */
public interface CollectNumDao {

	Integer getNumber(CollectNum collectNum);
	
	List<CollectNum> listByRelativeIdList(@Param("module") Integer module, @Param("relativeIdList") List<Long> relativeIdList);
}
