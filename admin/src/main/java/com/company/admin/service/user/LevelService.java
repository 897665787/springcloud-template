package com.company.admin.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.Level;
import com.company.admin.exception.ExceptionConsts;
import com.company.admin.mapper.user.LevelDao;

/**
 * 等级称号Service
 * Created by JQ棣 on 2018/06/21.
 */
@Service
public class LevelService {

    @Autowired
    private LevelDao levelDao;


    public void save(Level level) {
        List<Level> levelExisted = levelDao.findLevelExisted(level);
        if (levelExisted.size() > 0) throw ExceptionConsts.LEVEL_TITLE_EXISTED;
        levelDao.save(level);
    }


    public void remove(Level level) {
        Level existent = get(level);
        levelDao.remove(existent);
    }


    public void update(Level level) {
        Level existent = get(level);
        List<Level> levelExisted = levelDao.findLevelExisted(level);
        levelExisted.forEach(lt -> {
            if (!lt.getId().equals(existent.getId())) {
                throw ExceptionConsts.LEVEL_TITLE_EXISTED;
            }
        });
        levelDao.update(level);
    }


    public Level get(Level level) {
        Level existent = levelDao.get(level);
        if (existent == null) {
            throw ExceptionConsts.LEVEL_TITLE_NOT_EXIST;
        }
        return existent;
    }


    public XSPageModel<Level> listAndCount(Level level) {
        level.setDefaultSort("exp", "ASC");
        return XSPageModel.build(levelDao.list(level), levelDao.count(level));
    }
}
