package com.company.admin.mapper.system;

import com.company.admin.entity.system.DictionaryCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @author JQ棣
 * @date 2018/9/22
 */
public interface DictionaryCategoryDao {

    @Select("SELECT * FROM sc_dictionary_category WHERE id = #{id}")
    DictionaryCategory findById(Long id);

    @Delete("DELETE FROM sc_dictionary_category WHERE id = #{id}")
    int deleteById(Long id);

    int save(DictionaryCategory dictionaryCategory);

    int update(DictionaryCategory dictionaryCategory);

    List<DictionaryCategory> findAll(DictionaryCategory dictionaryCategory);

    long count(DictionaryCategory dictionaryCategory);

    @Select("SELECT id,`name` FROM sc_dictionary_category ORDER BY id DESC")
    List<DictionaryCategory> findCombo();

    @Select("SELECT COUNT(*) FROM sc_dictionary_category WHERE `key` = #{key}")
    boolean existByKey(String key);

    @Select("SELECT COUNT(*) FROM sc_dictionary_category WHERE `key` = #{key} AND id != #{id}")
    boolean existByKeyExcludeSelf(@Param("key") String key, @Param("id") Long id);

    @Select("SELECT * FROM sc_dictionary_category WHERE id = #{id} FOR UPDATE")
    DictionaryCategory findAndLockById(Long id);

    int updateLock(@Param("id") Long id, @Param("lock") Integer lock);
}
