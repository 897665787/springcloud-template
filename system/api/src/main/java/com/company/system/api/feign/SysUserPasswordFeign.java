package com.company.system.api.feign;

import com.company.common.api.Result;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.ThrowExceptionFallback;
import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.response.SysUserPasswordResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysUserPassword", fallbackFactory = ThrowExceptionFallback.class)
public interface SysUserPasswordFeign {

	@GetMapping("/getBySysUserId")
	Result<SysUserPasswordResp> getBySysUserId(@RequestParam("sysUserId") Integer sysUserId);

	@GetMapping("/getPasswordBySysUserId")
	Result<String> getPasswordBySysUserId(@RequestParam("sysUserId") Integer sysUserId);

	@PostMapping("/saveNewPassword")
	Result<Void> saveNewPassword(@RequestBody SaveNewPasswordReq saveNewPasswordReq);
}