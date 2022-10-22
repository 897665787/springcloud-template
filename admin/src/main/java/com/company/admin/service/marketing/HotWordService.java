package com.company.admin.service.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.marketing.HotWord;
import com.company.admin.mapper.marketing.HotWordDao;
import com.company.common.exception.BusinessException;

/**
 * 热搜词Service
 * Created by wjc on 2018/11/07.
 */
@Service
public class HotWordService {

    @Autowired
    private HotWordDao hotWordDao;

    public void save(HotWord hotWord) {
        hotWordDao.save(hotWord);
    }

    public void remove(HotWord hotWord) {
        HotWord existent = get(hotWord);
        hotWordDao.remove(existent);
    }

    public void update(HotWord hotWord) {
        HotWord existent = get(hotWord);
        hotWordDao.update(hotWord);
    }

    public HotWord get(HotWord hotWord) {
        HotWord existent = hotWordDao.get(hotWord);
        if (existent == null) {
            throw new BusinessException("热搜词不存在");
        }
        return existent;
    }

    public XSPageModel<HotWord> listAndCount(HotWord hotWord) {
        hotWord.setDefaultSort("id", "DESC");
        return XSPageModel.build(hotWordDao.list(hotWord), hotWordDao.count(hotWord));
    }
}
