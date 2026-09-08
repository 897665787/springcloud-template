package com.company.tool.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.CommonI18n;
import com.company.tool.mapper.CommonI18nMapper;

@Service
public class CommonI18nService extends ServiceImpl<CommonI18nMapper, CommonI18n> {
    public CommonI18n selectByBusinessTypeBusinessIdLocale(String businessType, Integer businessId, String locale) {
        return baseMapper.selectByBusinessTypeBusinessIdLocale(businessType, businessId, locale);
    }

    public List<CommonI18n> selectByBusinessTypeBusinessIdsLocale(String businessType, List<Integer> businessTypeIdList,
        String locale) {
        if (CollectionUtils.isEmpty(businessTypeIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectByBusinessTypeBusinessIdsLocale(businessType, businessTypeIdList, locale);
    }

    public List<CommonI18n> selectByBusinessTypesBusinessIdsLocale(List<CommonI18n> commonI18nList, String locale) {
        if (CollectionUtils.isEmpty(commonI18nList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectByBusinessTypesBusinessIdsLocale(commonI18nList, locale);
    }

}
