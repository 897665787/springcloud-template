package com.company.system.api.feign;

import com.company.common.api.Result;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.ThrowExceptionFallback;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysUserAssignRoleReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.PageResp;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysUser", fallbackFactory = ThrowExceptionFallback.class)
public interface SysUserFeign {

	@GetMapping("/page")
	Result<PageResp<SysUserResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "account", required = false) String account, @RequestParam(value = "nickname", required = false) String nickname, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phonenumber", required = false) String phonenumber, @RequestParam(value = "sex", required = false) String sex, @RequestParam(value = "avatar", required = false) String avatar, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "deptId", required = false) Integer deptId, @RequestParam(value = "userRemark", required = false) String userRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/list")
	Result<List<SysUserResp>> list(@RequestParam(value = "account", required = false) String account, @RequestParam(value = "nickname", required = false) String nickname, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phonenumber", required = false) String phonenumber, @RequestParam(value = "sex", required = false) String sex, @RequestParam(value = "avatar", required = false) String avatar, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "deptId", required = false) Integer deptId, @RequestParam(value = "userRemark", required = false) String userRemark, @RequestParam(value = "createTimeStart", required = false) String createTimeStart, @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd, @RequestParam(value = "updateTimeStart", required = false) String updateTimeStart, @RequestParam(value = "updateTimeEnd", required = false) String updateTimeEnd);

	@GetMapping("/query")
	Result<SysUserResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysUserReq sysUserReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysUserReq sysUserReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

	@GetMapping("/getByAccount")
	Result<SysUserResp> getByAccount(@RequestParam("account") String account);

	@GetMapping("/getById")
	Result<SysUserResp> getById(@RequestParam("id") Integer id);

	@GetMapping("/getInfo")
	Result<SysUserInfoResp> getInfo(@RequestParam("userId") Integer userId);

	@PostMapping("/getByBatchId")
	Result<List<SysUserResp>> getByBatchId(@RequestBody List<Integer> ids);

	@RequestMapping("/mapNicknameById")
	Result<Map<Integer, String>> mapNicknameById(@RequestBody Collection<Integer> ids);

	@PostMapping("/assignRole")
	Result<Boolean> assignRole(@RequestBody SysUserAssignRoleReq req);

}
