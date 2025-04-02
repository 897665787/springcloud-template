package com.company.admin.mapper.user;

import com.company.admin.entity.user.Level;

import java.util.List;

/**
 * 等级称号Dao
 * Created by JQ棣 on 2018/06/21.
 */
public interface LevelDao {

	int save(Level level);

	int remove(Level level);

	int update(Level level);

	Level get(Level level);

	List<Level> list(Level level);

	Long count(Level level);

    List<Level> findLevelExisted(Level level);
}
