package com.company.admin.mapper.system;

import com.company.admin.entity.system.Config;
import com.company.admin.entity.system.ConfigCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xxw
 * @date 2018/9/22
 */
public interface ConfigDao {

    Config findById(Long id);

    @Delete("DELETE FROM sc_config WHERE id = #{id}")
    int deleteById(Long id);

    int save(Config config);

    int update(Config config);

    List<Config> findAll(Config config);

    long count(Config config);

    @Select("SELECT COUNT(*) FROM sc_config WHERE `key` = #{key}")
    boolean existByKey(String key);

    @Select("SELECT COUNT(*) FROM sc_config WHERE `key` = #{key} AND id != #{id}")
    boolean existByKeyExcludeSelf(@Param("key") String key, @Param("id") Long id);

    @Select("SELECT `value` FROM sc_config WHERE `key` = #{key}")
    String findValueByKey(String key);

    List<Config> findByKeys(@Param("keys") String... keys);

    @Select("SELECT `key`,`value` FROM sc_config WHERE category_key = #{categoryKey}")
    List<Config> findByCategory(String categoryKey);

    @Select("SELECT COUNT(*) FROM sc_config WHERE category_key = #{categoryKey}")
    boolean existByCategory(String categoryKey);

    @Update("UPDATE sc_config SET category_key = #{newCategoryKey} WHERE category_key = #{categoryKey}")
    int updateByCategory(@Param("newCategoryKey") String newCategoryKey, @Param("categoryKey") String categoryKey);

    List<ConfigCategory> findByCategoryParent(Long categoryParentId);

    void batchUpdate(@Param("configs") List<Config> configs);
}
