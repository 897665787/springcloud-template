package com.company.admin.mapper.article;

import com.company.admin.entity.article.Article;

import java.util.List;

/**
 * 文章Dao
 * Created by JQ棣 on 2017/10/28.
 */
public interface ArticleDao {

    int save(Article article);

    int remove(Article article);

    int update(Article article);

    Article get(Article article);

    Article getByCate(Article article);

    List<Article> list(Article article);

    Long count(Article article);
}
