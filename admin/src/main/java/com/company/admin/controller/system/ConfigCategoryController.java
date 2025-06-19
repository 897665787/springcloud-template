package com.company.admin.controller.system;



import com.company.admin.service.system.ConfigCategoryService;
import com.company.common.api.Result;
import com.company.admin.entity.system.ConfigCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

/**
 * @author JQ棣
 * @date 2018/9/23
 */
@Controller
@RequestMapping("/admin/system/configCategory")
public class ConfigCategoryController {

    @Autowired
    private ConfigCategoryService configCategoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "system/configCategory";
    }

    @PostMapping("/save")
    @ResponseBody
    public ? save(@Validated(ConfigCategory.Save.class) ConfigCategory configCategory) {
        configCategoryService.save(configCategory);
        return null;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ? delete(@NotNull Long id) {
        configCategoryService.deleteById(id);
        return null;
    }

    @RequestMapping("/find")
    @ResponseBody
    public ? find(@NotNull Long id) {
        return configCategoryService.findById(id);
    }

    @PostMapping("/update")
    @ResponseBody
    public ? update(@Validated(ConfigCategory.Update.class) ConfigCategory configCategory) {
        configCategoryService.update(configCategory);
        return null;
    }

    @RequestMapping("/parent-drop-down-list")
    @ResponseBody
    public ? parentDropDownList() {
        return configCategoryService.findComboByParent(true);
    }

    @RequestMapping("/drop-down-list")
    @ResponseBody
    public ? dropDownList() {
        return configCategoryService.findComboByParent(false);
    }

    @RequestMapping("/tree")
    @ResponseBody
    public ? tree() {
        return configCategoryService.findTree();
    }
}
