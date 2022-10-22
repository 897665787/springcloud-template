package com.company.admin.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description: 插件controller
 * Creator: WDBB
 * Datetime: 2019/3/19 11:46
 */

@Controller
public class PluginsController {

    @GetMapping(value = "/admin/plugins/{name}")
    public String plugins(@PathVariable String name) {
        return "plugins/" + name;
    }
}
