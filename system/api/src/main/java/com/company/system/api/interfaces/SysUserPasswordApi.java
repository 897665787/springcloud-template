package com.company.system.api.interfaces;


import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.request.UpdatePasswordReq;
import com.company.system.api.response.SysUserPasswordTipsResp;

public interface SysUserPasswordApi {

	@GetMapping("/getPasswordBySysUserId")
    Map<String, String> getPasswordBySysUserId(@RequestParam("sysUserId") Integer sysUserId);

	@GetMapping("/getPasswordTipsBySysUserId")
	SysUserPasswordTipsResp getPasswordTipsBySysUserId(@RequestParam("sysUserId") Integer sysUserId);

	@PostMapping("/saveNewPassword")
	Void saveNewPassword(@RequestBody SaveNewPasswordReq saveNewPasswordReq);

	/**
	 * 用户自助修改密码（校验旧密码）
	 */
	@PostMapping("/changePassword")
	Boolean changePassword(@RequestParam("sysUserId") Integer sysUserId, @RequestBody UpdatePasswordReq updatePasswordReq);

	/**
	 * 管理员重置密码（不校验旧密码）
	 */
	@PostMapping("/resetPassword")
	Boolean resetPassword(@RequestBody SaveNewPasswordReq saveNewPasswordReq);
}