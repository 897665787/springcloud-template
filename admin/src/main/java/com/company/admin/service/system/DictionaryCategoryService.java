package com.company.admin.service.system;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.DictionaryCategory;
import com.company.admin.mapper.system.DictionaryCategoryDao;
import com.company.admin.mapper.system.DictionaryDao;
import com.company.common.exception.BusinessException;

/**
 * @author xxw
 * @date 2018/9/22
 */
@Service
public class DictionaryCategoryService {

    @Autowired
    private DictionaryCategoryDao dictionaryCategoryDao;

    @Autowired
    private DictionaryDao dictionaryDao;

    public DictionaryCategory findById(Long id) {
        DictionaryCategory existedDictionaryCategory = dictionaryCategoryDao.findById(id);
        if (existedDictionaryCategory == null) {
            throw new BusinessException("字典分类不存在");
        }
        return existedDictionaryCategory;
    }

    public void deleteById(Long id) {
        DictionaryCategory existedDictionaryCategory = findById(id);
        if (existedDictionaryCategory.getLock() == 1) {
            throw new BusinessException("字典分类被锁定");
        }
        boolean used = dictionaryDao.existByCategory(existedDictionaryCategory.getKey());
        if (used) {
            throw new BusinessException("字典分类被使用");
        }
        dictionaryCategoryDao.deleteById(id);
    }

    public void save(DictionaryCategory dictionaryCategory) {
        boolean existedKey = dictionaryCategoryDao.existByKey(dictionaryCategory.getKey());
        if (existedKey) {
            throw new BusinessException("键已存在");
        }
        dictionaryCategoryDao.save(dictionaryCategory);
    }

    @XSTransactional
    public void update(DictionaryCategory dictionaryCategory) {
        DictionaryCategory existedDictionaryCategory = findById(dictionaryCategory.getId());
        boolean existedKey = dictionaryCategoryDao.existByKeyExcludeSelf(dictionaryCategory.getKey(),
                dictionaryCategory.getId());
        if (existedKey) {
            throw new BusinessException("键已存在");
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

    @XSTransactional
    public void updateLock(Long id) {
        DictionaryCategory existedDictionaryCategory = dictionaryCategoryDao.findAndLockById(id);
        if (existedDictionaryCategory == null) {
            throw new BusinessException("字典分类不存在");
        }
        Integer lock = 1 - existedDictionaryCategory.getLock();
        dictionaryCategoryDao.updateLock(id, lock);
    }
}
