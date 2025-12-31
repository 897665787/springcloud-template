package com.company.admin.service.system;

import java.util.List;

import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.DictionaryCategory;
import com.company.admin.mapper.system.DictionaryCategoryDao;
import com.company.admin.mapper.system.DictionaryDao;

/**
 * @author JQ棣
 * @date 2018/9/22
 */
@Service
@RequiredArgsConstructor
public class DictionaryCategoryService {

    private final DictionaryCategoryDao dictionaryCategoryDao;

    private final DictionaryDao dictionaryDao;

    public DictionaryCategory findById(Long id) {
        DictionaryCategory existedDictionaryCategory = dictionaryCategoryDao.findById(id);
        if (existedDictionaryCategory == null) {
            ExceptionUtil.throwException("字典分类不存在");
        }
        return existedDictionaryCategory;
    }

    public void deleteById(Long id) {
        DictionaryCategory existedDictionaryCategory = findById(id);
        if (existedDictionaryCategory.getLock() == 1) {
            ExceptionUtil.throwException("字典分类被锁定");
        }
        boolean used = dictionaryDao.existByCategory(existedDictionaryCategory.getKey());
        if (used) {
            ExceptionUtil.throwException("字典分类被使用");
        }
        dictionaryCategoryDao.deleteById(id);
    }

    public void save(DictionaryCategory dictionaryCategory) {
        boolean existedKey = dictionaryCategoryDao.existByKey(dictionaryCategory.getKey());
        if (existedKey) {
            ExceptionUtil.throwException("键已存在");
        }
        dictionaryCategoryDao.save(dictionaryCategory);
    }

    @Transactional
    public void update(DictionaryCategory dictionaryCategory) {
        DictionaryCategory existedDictionaryCategory = findById(dictionaryCategory.getId());
        boolean existedKey = dictionaryCategoryDao.existByKeyExcludeSelf(dictionaryCategory.getKey(),
                dictionaryCategory.getId());
        if (existedKey) {
            ExceptionUtil.throwException("键已存在");
        }
        boolean changeKey = StringUtils.isNotBlank(dictionaryCategory.getKey()) &&
                !StringUtils.equals(dictionaryCategory.getKey(), existedDictionaryCategory.getKey());
        if (changeKey) {
            dictionaryDao.updateByCategory(dictionaryCategory.getKey(), existedDictionaryCategory.getKey());
        }
        
        existedDictionaryCategory.setName(dictionaryCategory.getName());
        existedDictionaryCategory.setKey(dictionaryCategory.getKey());
        existedDictionaryCategory.setComment(dictionaryCategory.getComment());
        existedDictionaryCategory.setLock(dictionaryCategory.getLock());
        
        dictionaryCategoryDao.update(existedDictionaryCategory);
    }

    public XSPageModel<DictionaryCategory> findAll(DictionaryCategory dictionaryCategory) {
        dictionaryCategory.setDefaultSort("id", "DESC");
        return XSPageModel.build(dictionaryCategoryDao.findAll(dictionaryCategory), dictionaryCategoryDao.count(dictionaryCategory));
    }

    public List<DictionaryCategory> findCombo() {
        return dictionaryCategoryDao.findCombo();
    }

    @Transactional
    public void updateLock(Long id) {
        DictionaryCategory existedDictionaryCategory = dictionaryCategoryDao.findAndLockById(id);
        if (existedDictionaryCategory == null) {
            ExceptionUtil.throwException("字典分类不存在");
        }
        Integer lock = 1 - existedDictionaryCategory.getLock();
        dictionaryCategoryDao.updateLock(id, lock);
    }
}
