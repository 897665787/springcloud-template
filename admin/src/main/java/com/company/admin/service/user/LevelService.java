package com.company.admin.service.user;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.Level;
import com.company.admin.mapper.user.LevelDao;
import com.company.framework.globalresponse.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (levelExisted.size() > 0) {
            ExceptionUtil.throwException("已存在相同等级或相同名称或相同经验值的的等级称号");
        }
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
                ExceptionUtil.throwException("已存在相同等级或相同名称或相同经验值的的等级称号");
            }
        });
        levelDao.update(level);
    }


    public Level get(Level level) {
        Level existent = levelDao.get(level);
        if (existent == null) {
            ExceptionUtil.throwException("等级称号不存在");
        }
        return existent;
    }


    public XSPageModel<Level> listAndCount(Level level) {
        level.setDefaultSort("exp", "ASC");
        return XSPageModel.build(levelDao.list(level), levelDao.count(level));
    }
}
