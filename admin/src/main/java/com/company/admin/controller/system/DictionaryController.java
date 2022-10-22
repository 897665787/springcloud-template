package com.company.admin.controller.system;



import com.company.admin.service.system.DictionaryService;
import com.company.common.api.Result;
import com.company.admin.entity.system.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author xxw
 * @date 2018/9/23
 */
@RestController


@RequestMapping("/admin/system/dictData")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping("/save")
    public Result<?> save(@Validated(Dictionary.Save.class) Dictionary dictionary) {
        dictionaryService.save(dictionary);
        return Result.success();
    }

    @RequestMapping("/remove")
    public Result<?> delete(@NotNull Long id) {
        dictionaryService.deleteById(id);
        return Result.success();
    }

    @RequestMapping("/get")
    public Result<?> find(@NotNull Long id) {
        return Result.success(dictionaryService.findById(id));
    }

    @PostMapping("/update")
    public Result<?> update(@Validated(Dictionary.Update.class) Dictionary dictionary) {
        dictionaryService.update(dictionary);
        return Result.success();
    }

    @RequestMapping("/lock/update")
    public Result<?> updateLock(@NotNull Long id) {
        dictionaryService.updateLock(id);
        return Result.success();
    }

    @RequestMapping(value = "/reload")
    @ResponseBody
    public Result<?> reload() {
        dictionaryService.invalidateCache();
        return Result.success();
    }
}
