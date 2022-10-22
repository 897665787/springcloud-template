package com.company.admin.controller.article;



import com.company.admin.service.article.ArticleCategoryService;
import com.company.common.api.Result;
import com.company.admin.entity.article.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文章分类
 * Created by gustinlau on 10/30/17.
 */
@Controller
@RequestMapping("/admin/content/article/category")
public class ArticleCategoryController {
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "content/articleCategory";
    }

    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminTree(ArticleCategory articleCategory) {
        articleCategory = articleCategory == null ? new ArticleCategory() : articleCategory;
        return Result.success(articleCategoryService.tree(articleCategory));
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminGet(ArticleCategory articleCategory) {
        return Result.success(articleCategoryService.get(articleCategory));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminSave(@Validated(ArticleCategory.Save.class) ArticleCategory articleCategory) {
        articleCategory.setLock(0);
        articleCategoryService.save(articleCategory);
        return Result.success();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminRemove(ArticleCategory articleCategory) {
        articleCategoryService.remove(articleCategory);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminUpdate(@Validated(ArticleCategory.Update.class) ArticleCategory articleCategory)
            {
        if (articleCategory.getParent() == null)
            articleCategory.setParent(new ArticleCategory());
        articleCategoryService.update(articleCategory);
        return Result.success();
    }

    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminLock(ArticleCategory articleCategory)
            {
        articleCategoryService.updateLock(articleCategory);
        return Result.success();
    }
}
