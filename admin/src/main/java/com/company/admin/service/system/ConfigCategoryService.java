package com.company.admin.service.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.ConfigCategory;
import com.company.admin.mapper.system.ConfigCategoryDao;
import com.company.admin.mapper.system.ConfigDao;
import com.company.admin.service.security.SecResourceService;
import com.company.admin.util.XSTreeUtil;
import com.company.common.exception.BusinessException;

/**
 * @author xxw
 * @date 2018/9/22
 */
@Service
public class ConfigCategoryService {

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private SecResourceService secResourceService;

    public ConfigCategory findById(Long id) {
        ConfigCategory existedConfigCategory = configCategoryDao.findById(id);
        if (existedConfigCategory == null) {
            throw new BusinessException("配置分类不存在");
        }
        return existedConfigCategory;
    }

    @XSTransactional
    public void deleteById(Long id) {
        ConfigCategory existedConfigCategory = findById(id);
        boolean used = configCategoryDao.existByParent(id) ||
                configDao.existByCategory(existedConfigCategory.getKey());
        if (used) {
            throw new BusinessException("配置分类被使用");
        }
        configCategoryDao.deleteById(id);
        if (existedConfigCategory.getParentId() == null) {
            secResourceService.removeDynamicResource(existedConfigCategory.getKey());
        }
    }

    @XSTransactional
    public void save(ConfigCategory configCategory) {
        boolean existedKey = configCategoryDao.existByKey(configCategory.getKey());
        if (existedKey) {
            throw new BusinessException("键已存在");
        }
        configCategoryDao.save(configCategory);
        if (configCategory.getParentId() == null) {
            secResourceService.saveDynamicResource(configCategory.getName(), configCategory.getKey());
        }
    }

    @XSTransactional
    public void update(ConfigCategory configCategory) {
        ConfigCategory existedConfigCategory = findById(configCategory.getId());
        boolean existedKey = configCategoryDao.existByKeyExcludeSelf(configCategory.getKey(), configCategory.getId());
        if (existedKey) {
            throw new BusinessException("键已存在");
        }
        boolean changeKey = StringUtils.isNotBlank(configCategory.getKey()) &&
                !StringUtils.equals(configCategory.getKey(), existedConfigCategory.getKey());
        if (changeKey) {
            configDao.updateByCategory(configCategory.getKey(), existedConfigCategory.getKey());
        }

        if (configCategory.getParentId() == null && existedConfigCategory.getParentId() == null) {
            secResourceService.updateDynamicResource(configCategory.getName(), configCategory.getKey(),
                    existedConfigCategory.getKey());
        }
        else if (configCategory.getParentId() == null && existedConfigCategory.getParentId() != null) {
            secResourceService.saveDynamicResource(configCategory.getName(), configCategory.getKey());
        }
        else if (configCategory.getParentId() != null && existedConfigCategory.getParentId() == null) {
            secResourceService.removeDynamicResource(existedConfigCategory.getKey());
        }

        existedConfigCategory.setName(configCategory.getName());
        existedConfigCategory.setKey(configCategory.getKey());
        existedConfigCategory.setComment(configCategory.getComment());
        
        existedConfigCategory.setParentId(configCategory.getParentId());
        configCategoryDao.update(existedConfigCategory);
    }

    public XSPageModel<ConfigCategory> list(ConfigCategory configCategory) {
        configCategory.setDefaultSort("cc.id", "DESC");
        return XSPageModel.build(configCategoryDao.findAll(configCategory), configCategoryDao.count(configCategory));
    }

    public List<ConfigCategory> findComboByParent(boolean parentIsNull) {
        return configCategoryDao.findComboByParent(parentIsNull);
    }

    public List<ConfigCategory> findTree() {
        List<ConfigCategory> categories = configCategoryDao.findAll(new ConfigCategory());
        Map<Long, ConfigCategory> treeMap = XSTreeUtil.buildTree(categories);
        return XSTreeUtil.getSubTrees(categories, null);
    }
}
