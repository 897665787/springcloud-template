package com.company.admin.controller.system;



import com.company.admin.service.system.AppInitService;
import com.company.common.api.Result;
import com.company.admin.entity.system.AppInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * App初始化Controller
 * Created by xuxiaowei on 2017/11/13.
 */
@Controller
public class AppInitController {

    @Autowired
    private AppInitService appInitService;

    @RequestMapping(value = "/admin/system/appInit", method = RequestMethod.GET)
    public String index(Model model, AppInit appInit) {
        if(appInit.getPage() == null) appInit.setPage(1L);
        model.addAttribute("search", appInit);
        model.addAttribute("pageModel", appInitService.listAndCount(appInit));
        return "system/appInit";
    }

    @RequestMapping(value = "/admin/system/appInit/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminGet(AppInit appInit) {
        return Result.success(appInitService.get(appInit));
    }

    @RequestMapping(value = "/admin/system/appInit/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminSave(@Validated(AppInit.Save.class) AppInit appInit) {
        appInitService.save(appInit);
        return Result.success();
    }

    @RequestMapping(value = "/admin/system/appInit/remove", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminRemove(AppInit appInit) {
        appInitService.remove(appInit);
        return Result.success();
    }

    @RequestMapping(value = "/admin/system/appInit/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminUpdate(@Validated(AppInit.Update.class) AppInit appInit) {
        appInitService.update(appInit);
        return Result.success();
    }
}
