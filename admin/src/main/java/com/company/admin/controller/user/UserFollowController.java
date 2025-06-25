package com.company.admin.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.admin.service.user.UserFollowService;
import com.company.common.api.Result;
import com.company.admin.entity.user.UserFollow;

@Controller
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @RequestMapping(value = "/admin/user/userFollow/list", method = RequestMethod.GET)
    @ResponseBody
    public ? list(UserFollow userFollow) {
        return userFollowService.listAndCount(userFollow);
    }

}
