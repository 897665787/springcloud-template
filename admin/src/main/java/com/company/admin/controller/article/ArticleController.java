package com.company.admin.controller.article;



import com.company.framework.util.JsonUtil;
import com.company.admin.service.article.ArticleCategoryService;
import com.company.admin.service.article.ArticleService;
import com.company.admin.annotation.Pagination;
import com.company.common.api.Result;
import com.company.admin.entity.article.Article;
import com.company.admin.entity.article.ArticleCategory;
import com.company.common.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文章管理
 * Created by JQ棣 on 10/30/17.
 */
@Controller
@RequestMapping("/admin/content/article/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Pagination
    public String index(Model model, Article article) throws Exception {
        model.addAttribute("search", article);
        model.addAttribute("pageModel", articleService.listAndCount(article));
        model.addAttribute("categoryTree", JsonUtil.toJsonString(articleCategoryService.tree(new ArticleCategory())));
        return "content/article";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) throws Exception {
        model.addAttribute("categoryTree", JsonUtil.toJsonString(articleCategoryService.tree(new ArticleCategory())));
        return "content/articleCreate";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(Model model,Article article) throws Exception {
        try {
            model.addAttribute("article", articleService.get(article));
        } catch (BusinessException e) {
            model.addAttribute("article", null);
        }
        model.addAttribute("categoryTree", JsonUtil.toJsonString(articleCategoryService.tree(new ArticleCategory())));
        return "content/articleUpdate";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminSave(@Validated(Article.Save.class) Article article) {
        articleService.save(article);
        return Result.success();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminRemove(Article article) {
        articleService.remove(article);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminUpdate(@Validated(Article.Update.class) Article article) {
        articleService.update(article);
        return Result.success();
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public String preview(Model model, Article article) throws Exception {
        model.addAttribute("article", articleService.get(article));
        return "content/articlePreview";
    }

}
