package com.company.system.api.feign;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysUserFeignFallback;
import com.company.system.api.request.SysUserAssignRoleReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysUser", fallbackFactory = SysUserFeignFallback.class)
public interface SysUserFeign {

	@GetMapping("/page")
	PageResp<SysUserResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "account", required = false) String account, @RequestParam(value = "nickname", required = false) String nickname, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phonenumber", required = false) String phonenumber, @RequestParam(value = "sex", required = false) String sex, @RequestParam(value = "avatar", required = false) String avatar, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "deptId", required = false) Integer deptId, @RequestParam(value = "userRemark", required = false) String userRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);
	
	@GetMapping("/list")
	List<SysUserResp> list(@RequestParam(value = "account", required = false) String account, @RequestParam(value = "nickname", required = false) String nickname, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phonenumber", required = false) String phonenumber, @RequestParam(value = "sex", required = false) String sex, @RequestParam(value = "avatar", required = false) String avatar, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "deptId", required = false) Integer deptId, @RequestParam(value = "userRemark", required = false) String userRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	SysUserResp query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Boolean save(@RequestBody SysUserReq sysUserReq);

	@PostMapping("/update")
	Boolean update(@RequestBody SysUserReq sysUserReq);

	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

	@GetMapping("/getByAccount")
	SysUserResp getByAccount(@RequestParam("account") String account);
	
	@GetMapping("/getById")
	SysUserResp getById(@RequestParam("id") Integer id);

	@GetMapping("/getInfo")
	SysUserInfoResp getInfo(@RequestParam("userId") Integer userId);

	@PostMapping("/getByBatchId")
	List<SysUserResp> getByBatchId(@RequestBody List<Integer> ids);

	@RequestMapping("/mapNicknameById")
	Map<Integer, String> mapNicknameById(@RequestBody Collection<Integer> ids);

	@PostMapping("/assignRole")
	Boolean assignRole(@RequestBody SysUserAssignRoleReq req);

}
