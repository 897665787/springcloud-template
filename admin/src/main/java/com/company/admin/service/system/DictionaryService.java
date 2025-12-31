package com.company.admin.service.system;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.Dictionary;
import com.company.admin.entity.system.DictionaryCategory;
import com.company.admin.mapper.system.DictionaryDao;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author JQ棣
 * @date 2018/9/22
 */
@Service
@RequiredArgsConstructor
public class DictionaryService {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryService.class);

    private final DictionaryDao dictionaryDao;

    private final DictionaryCategoryService dictionaryCategoryService;

    private Cache<String, Map<String, String>> cache = null;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder().expireAfterAccess(1800L, TimeUnit.SECONDS).maximumSize(512).build();
    }

    public Dictionary findById(Long id) {
        Dictionary existedDictionary = dictionaryDao.findById(id);
        if (existedDictionary == null) {
            ExceptionUtil.throwException("字典不存在");
        }
        return existedDictionary;
    }

    public void deleteById(Long id) {
        Dictionary existedDictionary = findById(id);
        if (existedDictionary.getLock() == 1) {
            ExceptionUtil.throwException("字典被锁定");
        }
        dictionaryDao.deleteById(id);
        //清除该数据所属字典的缓存
        cache.invalidate(existedDictionary.getCategoryKey());
    }

    public void save(Dictionary dictionary) {
        DictionaryCategory dictionaryCategory = dictionaryCategoryService.findById(dictionary.getCategoryId());
        dictionary.setCategoryKey(dictionaryCategory.getKey());
        boolean existedKey = dictionaryDao.existByCategoryAndKey(dictionary.getCategoryKey(), dictionary.getKey());
        if (existedKey) {
            ExceptionUtil.throwException("键已存在");
        }
        dictionaryDao.save(dictionary);
        //清除该数据所属字典的缓存
        cache.invalidate(dictionaryCategory.getKey());
    }

    public void update(Dictionary dictionary) {
        Dictionary existedDictionary = findById(dictionary.getId());
        DictionaryCategory dictionaryCategory = dictionaryCategoryService.findById(dictionary.getCategoryId());
        dictionary.setCategoryKey(dictionaryCategory.getKey());
        boolean existedKey = dictionaryDao.existByCategoryAndKeyExcludeSelf(dictionary.getCategoryKey(),
                dictionary.getKey(), dictionary.getId());
        if (existedKey) {
            ExceptionUtil.throwException("键已存在");
        }
        
        existedDictionary.setKey(dictionary.getKey());
        existedDictionary.setValue(dictionary.getValue());
        existedDictionary.setComment(dictionary.getComment());
        existedDictionary.setLock(dictionary.getLock());
        existedDictionary.setSeq(dictionary.getSeq());
        existedDictionary.setStatus(dictionary.getStatus());
        existedDictionary.setCategoryId(dictionary.getCategoryId());
        existedDictionary.setCategoryName(dictionary.getCategoryName());
        existedDictionary.setCategoryKey(dictionary.getCategoryKey());
        existedDictionary.setIcon(dictionary.getIcon());
        existedDictionary.setColor(dictionary.getColor());
        existedDictionary.setChecked(dictionary.getChecked());
        
        dictionaryDao.update(existedDictionary);
        //清除该数据所属字典的缓存
        cache.invalidate(existedDictionary.getCategoryKey());
    }

    public XSPageModel<Dictionary> findAll(Dictionary dictionary) {
        dictionary.setDefaultSort("d.id", "DESC");
        return XSPageModel.build(dictionaryDao.findAll(dictionary), dictionaryDao.count(dictionary));
    }

    @Transactional
    public void updateLock(Long id) {
        Dictionary existedDictionary = dictionaryDao.findAndLockById(id);
        if (existedDictionary == null) {
            ExceptionUtil.throwException("字典不存在");
        }
        Integer lock = 1 - existedDictionary.getLock();
        dictionaryDao.updateLock(id, lock);
    }

    public Optional<String> findByKey(String categoryKey, String key) {
        return Optional.ofNullable(mapByCategory(categoryKey).get(key));
    }

    public List<Dictionary> findByCategory(String categoryKey) {
        return dictionaryDao.findByCategory(categoryKey);
    }

    public Map<String, String> mapByCategory(String categoryKey) {
        Map<String, String> map = null;
        //先查询缓存，若未命中则查询数据库
        try {
            map = cache.get(categoryKey, new Callable<Map<String, String>>() {
                @Override
                public Map<String, String> call() {
                    logger.debug("Guava缓存未命中，从数据库中获取字典数据，key=" + categoryKey);
                    List<Dictionary> dictionaries = dictionaryDao.findByCategory(categoryKey);
                    Map<String, String> map = new LinkedHashMap<>();
                    for (Dictionary dictionary : dictionaries) {
                        map.put(dictionary.getKey(), dictionary.getValue());
                    }
                    return map;
                }
            });
        } catch (Exception e) {
            logger.error("error : ", e);
        }
        return map == null ? new HashMap<>() : map;

    }

    public void invalidateCache() {
        cache.invalidateAll();
    }
}
