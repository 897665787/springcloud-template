package com.company.system.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysRoleFeignFallback;
import com.company.system.api.request.SysRoleGrantMenuReq;
import com.company.system.api.request.SysRoleReq;
import com.company.system.api.response.SysRoleResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysRole", fallbackFactory = SysRoleFeignFallback.class)
public interface SysRoleFeign {

	@GetMapping("/page")
	Result<PageResp<SysRoleResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "roleName", required = false) String roleName, @RequestParam(value = "roleKey", required = false) String roleKey, @RequestParam(value = "roleSort", required = false) Integer roleSort, @RequestParam(value = "dataScope", required = false) String dataScope, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "roleRemark", required = false) String roleRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/list")
	Result<List<SysRoleResp>> list(@RequestParam(value = "roleName", required = false) String roleName, @RequestParam(value = "roleKey", required = false) String roleKey, @RequestParam(value = "roleSort", required = false) Integer roleSort, @RequestParam(value = "dataScope", required = false) String dataScope, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "roleRemark", required = false) String roleRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	Result<SysRoleResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysRoleReq sysRoleReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysRoleReq sysRoleReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

	@PostMapping("/grantMenu")
	Result<Boolean> grantMenu(@RequestBody SysRoleGrantMenuReq req);

}
