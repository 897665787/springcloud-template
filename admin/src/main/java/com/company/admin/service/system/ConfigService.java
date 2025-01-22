package com.company.admin.service.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.system.Config;
import com.company.admin.entity.system.ConfigCategory;
import com.company.admin.mapper.system.ConfigDao;
import com.company.admin.service.security.SecStaffService;
import com.company.common.exception.BusinessException;

/**
 * @author JQ棣
 * @date 2018/9/22
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigCategoryService configCategoryService;
    
    @Autowired
    private SecStaffService secStaffService;

    public Config findById(Long id) {
        Config existedConfig = configDao.findById(id);
        if (existedConfig == null) {
            throw new BusinessException("配置不存在");
        }
        return existedConfig;
    }

    public void deleteById(Long id) {
        configDao.deleteById(id);
    }

    public void save(Config config) {
        boolean existedKey = configDao.existByKey(config.getKey());
        if (existedKey) {
            throw new BusinessException("键已存在");
        }
        ConfigCategory configCategory = configCategoryService.findById(config.getCategoryId());
        config.setCategoryKey(configCategory.getKey());
        configDao.save(config);
    }

    public void update(Config config) {
        Config existedConfig = findById(config.getId());
        boolean existedKey = configDao.existByKeyExcludeSelf(config.getKey(), config.getId());
        if (existedKey) {
            throw new BusinessException("键已存在");
        }
        ConfigCategory configCategory = configCategoryService.findById(config.getCategoryId());
        config.setCategoryKey(configCategory.getKey());
        
        existedConfig.setName(config.getName());
        existedConfig.setKey(config.getKey());
        existedConfig.setValue(config.getValue());
        existedConfig.setType(config.getType());
        existedConfig.setComment(config.getComment());
        existedConfig.setCategoryId(config.getCategoryId());
        existedConfig.setCategoryName(config.getCategoryName());
        existedConfig.setCategoryKey(config.getCategoryKey());
        
        configDao.update(existedConfig);
    }

    public XSPageModel<Config> list(Config config) {
        config.setDefaultSort("co.id", "DESC");
        return XSPageModel.build(configDao.findAll(config), configDao.count(config));
    }

    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(configDao.findValueByKey(key));
    }

    public String findByKey(String key, String defaultValue) {
        Optional<String> value = findByKey(key);
        return value.isPresent() ? value.get() : defaultValue;
    }

    public Map<String, String> findByKeys(String... keys) {
        Map<String, String> map = new HashMap<>(16);
        if (keys.length > 0) {
            configDao.findByKeys(keys).stream().forEach(config -> map.put(config.getKey(), config.getValue()));
        }
        return map;
    }

    public Map<String, String> findByCategory(String categoryKey) {
        Map<String, String> map = new HashMap<>(16);
        configDao.findByCategory(categoryKey).stream().forEach(config -> map.put(config.getKey(), config.getValue()));
        return map;
    }

    public List<ConfigCategory> findByCategoryParent(Long categoryParentId) {
        return configDao.findByCategoryParent(categoryParentId);
    }

    public void batchUpdate(List<Config> configs) {
        if (CollectionUtils.isNotEmpty(configs)) {
            configDao.batchUpdate(configs);
        }
    }

    public List<ConfigCategory> findCategoryParent() {
        List<ConfigCategory> configCategories = configCategoryService.findComboByParent(true);
        
        SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
		List<SecResource> owedResources = secStaff.getResourceList();
        for(Iterator<ConfigCategory> iterator = configCategories.iterator(); iterator.hasNext();) {
            ConfigCategory configCategory = iterator.next();
            boolean visible = false;
            for (SecResource secResource : owedResources) {
                if (("dynamic_config_" + configCategory.getKey()).equals(secResource.getKey())) {
                    visible = true;
                    break;
                }
            }
            if (!visible) {
                iterator.remove();
            }
        }
        return configCategories;
    }
}
