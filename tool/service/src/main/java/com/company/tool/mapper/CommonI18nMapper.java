package com.company.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.CommonI18n;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommonI18nMapper extends BaseMapper<CommonI18n> {

    CommonI18n selectByBusinessTypeBusinessIdLocale(@Param("businessType") String businessType,
        @Param("businessId") Integer businessId, @Param("locale") String locale);

    List<CommonI18n> selectByBusinessTypeBusinessIdsLocale(@Param("businessType") String businessType,
        @Param("businessTypeIds") List<Integer> businessTypeIds, @Param("locale") String locale);

    List<CommonI18n> selectByBusinessTypesBusinessIdsLocale(@Param("commonI18nList") List<CommonI18n> commonI18nList,
        @Param("locale") String locale);
}