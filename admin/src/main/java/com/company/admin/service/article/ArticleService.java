package com.company.admin.service.article;

import com.company.admin.entity.article.Article;
import com.company.admin.entity.article.ArticleCategory;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.mapper.article.ArticleDao;
import com.company.admin.util.XSUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章ServiceImpl
 * Created by JQ棣 on 2017/10/28.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    public void save(Article article) {
        article.setId(XSUuidUtil.generate());
        articleDao.save(article);
    }

    public void remove(Article article) {
        articleDao.remove(article);
    }

    public void update(Article article) {
        articleDao.update(article);
    }

    public Article get(Article article) {
        Article existent = articleDao.get(article);
        return existent;
    }

    public Article getByCate(Article article) {
        return articleDao.getByCate(article);
    }

    public XSPageModel<Article> listAndCount(Article article) {
        article.setDefaultSort(new String[]{"a.seq","a.create_time"},new String[]{"DESC","DESC"});
        if (article.getCategory() != null && article.getCategory().getId() != null) {
            List<String> cateList = articleCategoryService.listSubTree(article.getCategory().getId());
            article.setCateList(cateList);
        }

        if (article.getCategory() != null && article.getCategory().getKey() != null) {
            ArticleCategory c = articleCategoryService.getByKey(article.getCategory().getKey());
            List<String> cateList = articleCategoryService.listSubTree(c.getId());
            article.setCateList(cateList);
        }
        return XSPageModel.build(articleDao.list(article), articleDao.count(article));
    }

}
