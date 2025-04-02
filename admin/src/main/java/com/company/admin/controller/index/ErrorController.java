package com.company.admin.controller.index;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by JQ棣 on 05/09/2017.
 */
@Controller
@RequestMapping("/admin/error")
public class ErrorController {

    @RequestMapping("/403")
    public String deny(Model model){
        model.addAttribute("code",403);
        model.addAttribute("message","您的访问被拒绝！");
        return "common/error";
    }

    @RequestMapping("/404")
    public String notFound(Model model){
        model.addAttribute("code",404);
        model.addAttribute("message","页面不存在或已被删除！");
        return "common/error";
    }

    @RequestMapping("/500")
    public String inner(Model model){
        model.addAttribute("code",500);
        model.addAttribute("message","服务器内部发生错误！");
        return "common/error";
    }
}
