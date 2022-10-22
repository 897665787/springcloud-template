package com.company.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.common.api.Result;
import com.company.admin.entity.system.Version;
import com.company.admin.service.system.VersionService;


/**
 * Created by gustinlau on 11/1/17.
 */
@Controller
public class VersionController {

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "/admin/system/version", method = RequestMethod.GET)
    public String index(Model model, Version version) {
        model.addAttribute("search", version);
        model.addAttribute("pageModel", versionService.listAndCount(version));
        return "system/version";
    }

    @RequestMapping(value = "/admin/system/version/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminGet(Version version) {
        return Result.success(versionService.get(version));
    }

    @RequestMapping(value = "/admin/system/version/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminSave(@Validated(Version.Save.class) Version version) {
        versionService.save(version);
        return Result.success();
    }

    @RequestMapping(value = "/admin/system/version/remove", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminRemove(Version version) {
        versionService.remove(version);
        return Result.success();
    }

    @RequestMapping(value = "/admin/system/version/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminUpdate(@Validated(Version.Update.class) Version version) {
        versionService.update(version);
        return Result.success();
    }
}
