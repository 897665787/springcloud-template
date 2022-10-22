package com.company.admin.controller.system;



import com.company.admin.service.system.DictionaryCategoryService;
import com.company.admin.service.system.DictionaryService;
import com.company.common.api.Result;
import com.company.admin.entity.system.Dictionary;
import com.company.admin.entity.system.DictionaryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xxw
 * @date 2018/9/23
 */
@Controller
@RequestMapping("/admin/system/dict")
public class DictionaryCategoryController {

    @Autowired
    private DictionaryCategoryService dictionaryCategoryService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("")
    public String list(DictionaryCategory dictionaryCategory, Model model) {
        if (dictionaryCategory.getPage() == null) {
            dictionaryCategory.setPage(0L);
            dictionaryCategory.setLimit(10L);
        }
        model.addAttribute("search", dictionaryCategory);
        model.addAttribute("pageModel", dictionaryCategoryService.findAll(dictionaryCategory));
        return "system/dictionaryCategory";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@Validated(DictionaryCategory.Save.class) DictionaryCategory dictionaryCategory) {
        dictionaryCategoryService.save(dictionaryCategory);
        return Result.success();
    }

    @RequestMapping("/remove")
    @ResponseBody
    public Result<?> delete(@NotNull Long id) {
        dictionaryCategoryService.deleteById(id);
        return Result.success();
    }

    @RequestMapping("/get")
    public String find(@NotNull Long id, Model model) {
        DictionaryCategory category = dictionaryCategoryService.findById(id);
        Dictionary dictionary = new Dictionary();
        dictionary.setCategoryId(category.getId());
        List<Dictionary> dictionaries = dictionaryService.findAll(dictionary).getList();
        model.addAttribute("category", category);
        model.addAttribute("dictionaries", dictionaries);
        return "system/dictionary";
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<?> update(@Validated(DictionaryCategory.Update.class) DictionaryCategory dictionaryCategory) {
        dictionaryCategoryService.update(dictionaryCategory);
        return Result.success();
    }

    @RequestMapping("/lock/update")
    @ResponseBody
    public Result<?> updateLock(@NotNull Long id) {
        dictionaryCategoryService.updateLock(id);
        return Result.success();
    }
}
