package com.company.admin.mapper.system;

import com.company.admin.entity.system.ConfigCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JQ棣
 * @date 2018/9/22
 */
public interface ConfigCategoryDao {

    ConfigCategory findById(Long id);

    @Delete("DELETE FROM sc_config_category WHERE id = #{id}")
    int deleteById(Long id);

    int save(ConfigCategory configCategory);

    int update(ConfigCategory configCategory);

    List<ConfigCategory> findAll(ConfigCategory configCategory);

    long count(ConfigCategory configCategory);

    @Select("SELECT COUNT(*) FROM sc_config_category WHERE parent_id = #{parentId}")
    boolean existByParent(Long parentId);

    @Select("SELECT COUNT(*) FROM sc_config_category WHERE `key` = #{key}")
    boolean existByKey(String key);

    @Select("SELECT COUNT(*) FROM sc_config_category WHERE `key` = #{key} AND id != #{id}")
    boolean existByKeyExcludeSelf(@Param("key") String key, @Param("id") Long id);

    List<ConfigCategory> findComboByParent(@Param("parentIsNull") boolean parentIsNull);
}
