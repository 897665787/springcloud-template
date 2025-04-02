package com.company.admin.controller.index;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.company.admin.exception.ExceptionConsts;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JQ棣 on 26/10/2017.
 */
@Controller("adminLoginController")
public class LoginController {

    @RequestMapping(value = {"", "admin"})
    public String admin(HttpServletRequest request){
        if (request.getSession().getAttribute("SPRING_SECURITY_CONTEXT")==null) {
            return "redirect:/admin/login";
        } else {
            return "redirect:/admin/index";
        }
    }

    @RequestMapping("admin/login")
    public ModelAndView loginPage(HttpServletRequest request) throws IOException {
        if (request.getHeader("X-Requested-With") == null) {
            return new ModelAndView("login");
        }
        else {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            Map<String, Object> result = new HashMap<>();
            result.put("status", false);
            result.put("code", ExceptionConsts.SEC_STAFF_SESSION_TIMEOUT.getCode());
            result.put("message", ExceptionConsts.SEC_STAFF_SESSION_TIMEOUT.getMessage());
            view.setAttributesMap(result);
            return new ModelAndView(view);
        }
    }

    @RequestMapping(value = "admin/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        return redirectUrl("index");
    }

    private String redirectUrl(String url) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
                .getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContextImpl == null) {
            return "login";
        }
        return url;
    }
}
