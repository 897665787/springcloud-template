package com.company.admin.mapper.interact;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.company.admin.entity.interact.CommentNum;

/**
 * 评论数Dao
 * Created by JQ棣 on 2018/11/09.
 */
public interface CommentNumDao {
	
	int numberChange(CommentNum commentNum);
	Integer getNumber(CommentNum commentNum);

	List<CommentNum> listByRelativeIdList(@Param("module") Integer module, @Param("relativeIdList") List<Long> relativeIdList);
}
