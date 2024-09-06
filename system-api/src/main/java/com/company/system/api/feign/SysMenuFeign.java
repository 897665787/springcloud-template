package com.company.system.api.feign;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysMenuFeignFallback;
import com.company.system.api.request.SysMenuReq;
import com.company.system.api.response.RouterResp;
import com.company.system.api.response.SysMenuResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysMenu", fallbackFactory = SysMenuFeignFallback.class)
public interface SysMenuFeign {

	@GetMapping("/page")
	Result<PageResp<SysMenuResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size,
									   @RequestParam(value = "parentId", required = false) Integer parentId,
									   @RequestParam(value = "menuName", required = false) String menuName,
									   @RequestParam(value = "menuType", required = false) String menuType,
									   @RequestParam(value = "status", required = false) String status,
									   @RequestParam(value = "visible", required = false) Integer visible,
									   @RequestParam(value = "perms", required = false) String perms,
									   @RequestParam(value = "createTimeStart", required = false) String createTimeStart,
									   @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd);
	
	@GetMapping("/list")
	Result<List<SysMenuResp>> list(@RequestParam(value = "parentId", required = false) Integer parentId,
								   @RequestParam(value = "menuName", required = false) String menuName,
								   @RequestParam(value = "orderNum", required = false) Integer orderNum,
								   @RequestParam(value = "menuType", required = false) String menuType,
								   @RequestParam(value = "status", required = false) String status,
								   @RequestParam(value = "visible", required = false) Integer visible,
								   @RequestParam(value = "perms", required = false) String perms,
								   @RequestParam(value = "createTimeStart", required = false) String createTimeStart,
								   @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd);

	@GetMapping("/query")
	Result<SysMenuResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysMenuReq sysMenuReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysMenuReq sysMenuReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

	@GetMapping("/getRouters")
	Result<List<RouterResp>> getRouters(@RequestParam("UserId") Integer userId);
}