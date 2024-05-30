package com.company.adminapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.annotation.RequirePermissions;
import com.company.common.api.Result;
import com.company.user.api.response.UserResp;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

//	@Autowired
//	private UserFeign userFeign;
	
    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
	@RequirePermissions("system:config:add")
	@GetMapping(value = "/getInfo")
	public Result<UserResp> getInfo() {
		return Result.success();
	}

	@RequirePermissions("system:config:routers")
	@GetMapping(value = "/getRouters")
	public Result<UserResp> getRouters() {
		return Result.success();
	}
	
	@GetMapping(value = "/no")
	public Result<UserResp> no() {
		return Result.success();
	}
}
