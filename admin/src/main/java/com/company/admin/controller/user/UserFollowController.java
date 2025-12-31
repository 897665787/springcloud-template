package com.company.admin.controller.user;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.admin.service.user.UserFollowService;

import com.company.admin.entity.user.UserFollow;

@Controller
public class UserFollowController {

    private final UserFollowService userFollowService;

    @RequestMapping(value = "/admin/user/userFollow/list", method = RequestMethod.GET)
    @ResponseBody
    public XSPageModel<User> list(UserFollow userFollow) {
        return userFollowService.listAndCount(userFollow);
    }
    
}
