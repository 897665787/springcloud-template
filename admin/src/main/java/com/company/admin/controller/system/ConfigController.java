package com.company.admin.controller.system;



import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.admin.entity.system.Config;
import com.company.admin.entity.system.ConfigCategory;
import com.company.admin.service.system.ConfigService;
import com.company.common.api.Result;

/**
 * @author xxw
 * @date 2018/9/23
 */
@Controller
@RequestMapping("/admin/system/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, Long categoryParentId) {
        List<ConfigCategory> configCategoryParents = configService.findCategoryParent();
        model.addAttribute("configCategoryParents", configCategoryParents);
        if (CollectionUtils.isNotEmpty(configCategoryParents)) {
            if (categoryParentId == null) {
                categoryParentId = configCategoryParents.iterator().next().getId();
            }
            model.addAttribute("categoryParentId", categoryParentId);
            model.addAttribute("configCategories", configService.findByCategoryParent(categoryParentId));
        }
        return "system/config";
    }

//    @PostMapping("/list")
//    @ResponseBody
//    public Result<?> list(@RequestBody(required = false) Config config) {
//        return Result.success(configService.list(config));
//    }
//
    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@Validated(Config.Save.class) Config config) {
        configService.save(config);
        return Result.success();
    }

    @RequestMapping("/remove")
    @ResponseBody
    public Result<?> delete(@NotNull Long id) {
        configService.deleteById(id);
        return Result.success();
    }
//
//    @RequestMapping(value = "/find")
//    @ResponseBody
//    public Result<?> find(@NotNull Long id) {
//        return Result.success(configService.findById(id));
//    }
//
//    @PostMapping("/update")
//    @ResponseBody
//    public Result<?> update(@Validated(Config.Update.class) @RequestBody Config config) {
//        configService.update(config);
//        return Result.success();
//    }

    @PostMapping("/batchUpdate")
    @ResponseBody
    public Result<?> batchUpdate(@RequestBody List<Config> configs) {
        configService.batchUpdate(configs);
        return Result.success();
    }
}
