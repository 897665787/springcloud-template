package com.company.adminapi.controller;

import com.company.common.api.Result;
import com.company.framework.annotation.RequireLogin;
import com.company.system.api.response.SysUserInfoResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequireLogin
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
	@GetMapping(value = "/getInfo")
	public Result<SysUserInfoResp> getInfo() {
		return Result.success();
	}
}
