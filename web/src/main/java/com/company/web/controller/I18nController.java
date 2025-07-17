package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.globalresponse.ExceptionUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/i8n")
public class I18nController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/accept-language")
    public Result<Map<String, String>> acceptLanguage() {
        String hello1 = messageSource.getMessage("test.hello", null, "Default message", LocaleContextHolder.getLocale());
        String hello2 = messageSource.getMessage("test.hello.name", new Object[]{"zhangsan"}, "Default message", LocaleContextHolder.getLocale());
        Map<String, String> result = Maps.newHashMap();
        result.put("hello1", hello1);
        result.put("hello2", hello2);

        return Result.success(result);
    }

    @GetMapping(value = "/lang")
    public Result<Map<String, String>> lang(String lang) {
        String hello1 = messageSource.getMessage("test.hello", null, "Default message", LocaleContextHolder.getLocale());
        String hello2 = messageSource.getMessage("test.hello.name", new Object[]{"zhangsan"}, "Default message", LocaleContextHolder.getLocale());
        Map<String, String> result = Maps.newHashMap();
        result.put("hello1", hello1);
        result.put("hello2", hello2);

        return Result.success(result);
    }

    @GetMapping(value = "/exception")
    public Result<Map<String, String>> exception() {
        if (true) {
//            ExceptionUtil.throwArgsException("ID不能为空");
            ExceptionUtil.throwException("ID{0}不能为空", 1);
//            ExceptionUtil.throwException("ID，不能为空");
//            ExceptionUtil.throwException("ID 不能为空");// 带空格不起效果
        }
        Map<String, String> result = Maps.newHashMap();
        return Result.success(result);
    }
}
