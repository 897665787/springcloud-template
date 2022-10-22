package com.company.admin.mapper.system;

import com.company.admin.entity.system.Dictionary;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * @author xxw
 * @date 2018/9/22
 */
public interface DictionaryDao {

    Dictionary findById(Long id);

    @Delete("DELETE FROM sc_dictionary WHERE id = #{id}")
    int deleteById(Long id);

    int save(Dictionary dictionary);

    int update(Dictionary dictionary);

    List<Dictionary> findAll(Dictionary dictionary);

    long count(Dictionary dictionary);

    @Select("SELECT COUNT(*) FROM sc_dictionary WHERE category_key = #{categoryKey} AND `key` = #{key}")
    boolean existByCategoryAndKey(@Param("categoryKey") String categoryKey, @Param("key") String key);

    @Select("SELECT COUNT(*) FROM sc_dictionary WHERE category_key = #{categoryKey} AND `key` = #{key} AND id != #{id}")
    boolean existByCategoryAndKeyExcludeSelf(@Param("categoryKey") String categoryKey, @Param("key") String key,
                                             @Param("id") Long id);

    @Select("SELECT * FROM sc_dictionary WHERE id = #{id} FOR UPDATE")
    Dictionary findAndLockById(Long id);

    int updateLock(@Param("id") Long id, @Param("lock") Integer lock);

    @Select("SELECT `value`,icon,color FROM sc_dictionary WHERE category_key = #{categoryKey} AND `key` = #{key}")
    String findValueByCategoryAndKey(@Param("categoryKey") String categoryKey, @Param("key") String key);

    @Select("SELECT `key`,`value`,icon,color FROM sc_dictionary WHERE category_key = #{categoryKey} AND status = 1 ORDER BY seq DESC")
    List<Dictionary> findByCategory(String categoryKey);

    @Select("SELECT COUNT(*) FROM sc_dictionary WHERE category_key = #{categoryKey}")
    boolean existByCategory(String categoryKey);

    @Update("UPDATE sc_dictionary SET category_key = #{newCategoryKey} WHERE category_key = #{categoryKey}")
    int updateByCategory(@Param("newCategoryKey") String newCategoryKey, @Param("categoryKey") String categoryKey);
}
