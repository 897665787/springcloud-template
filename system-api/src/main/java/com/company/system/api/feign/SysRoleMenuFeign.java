package com.company.system.api.feign;

import java.util.List;

import com.company.common.request.RemoveReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysRoleMenuFeignFallback;
import com.company.system.api.request.SysRoleMenuReq;
import com.company.system.api.response.SysRoleMenuResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysRoleMenu", fallbackFactory = SysRoleMenuFeignFallback.class)
public interface SysRoleMenuFeign {

	@GetMapping("/page")
	Result<PageResp<SysRoleMenuResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "roleId", required = false) Integer roleId, @RequestParam(value = "menuId", required = false) Integer menuId, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);
	
	@GetMapping("/list")
	Result<List<SysRoleMenuResp>> list(@RequestParam(value = "roleId", required = false) Integer roleId, @RequestParam(value = "menuId", required = false) Integer menuId, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	Result<SysRoleMenuResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysRoleMenuReq sysRoleMenuReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysRoleMenuReq sysRoleMenuReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

}