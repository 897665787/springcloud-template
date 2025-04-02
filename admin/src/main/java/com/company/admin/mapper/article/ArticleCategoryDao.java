package com.company.admin.mapper.article;

import com.company.admin.entity.article.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章分类Dao
 * Created by JQ棣 on 2017/10/28.
 */
public interface ArticleCategoryDao {

    int save(ArticleCategory articleCategory);

    int remove(ArticleCategory articleCategory);

    int update(ArticleCategory articleCategory);

    int batchUpdate(ArticleCategory articleCategory);

    int batchUpdateLock(ArticleCategory articleCategory);

    ArticleCategory get(ArticleCategory articleCategory);

    List<ArticleCategory> list(ArticleCategory articleCategory);

    List<ArticleCategory> listCombo(ArticleCategory articleCategory);

    Long count(ArticleCategory articleCategory);

    ArticleCategory getByKey(@Param("key") String key);
}
