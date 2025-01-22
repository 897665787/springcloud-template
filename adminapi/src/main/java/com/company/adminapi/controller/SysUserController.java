package com.company.adminapi.controller;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.controller.converter.SysUserIdNicknameConverter;
import com.company.adminapi.converter.annotation.RespConverter;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.excel.SysUserExcel;
import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.framework.annotation.RequireLogin;
import com.company.system.api.feign.SysUserFeign;
import com.company.system.api.feign.SysUserPasswordFeign;
import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;

@RequireLogin
@RestController
@RequestMapping("/sysUser")
public class SysUserController {


	@Autowired
	private SysUserFeign sysUserFeign;
	@Autowired
	private SysUserPasswordFeign sysUserPasswordFeign;

	@RespConverter(field = "createBy", newField = "createByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RespConverter(field = "updateBy", newField = "updateByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RequirePermissions("system:sysUser:query")
	@GetMapping("/page")
	public Result<PageResp<SysUserResp>> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		return sysUserFeign.page(current, size, account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
	}

	@RespConverter(field = "createBy", newField = "createByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RespConverter(field = "updateBy", newField = "updateByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RequirePermissions("system:sysUser:query")
	@GetMapping("/query")
	public Result<SysUserResp> query(@NotNull Integer id) {
		return sysUserFeign.query(id);
	}

	@OperationLog(title = "用户信息保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:sysUser:save")
	@PostMapping("/save")
	public Result<Boolean> save(@RequestBody SysUserReq sysUserReq) {
		sysUserFeign.save(sysUserReq).dataOrThrow();
		SysUserResp sysUserResp = sysUserFeign.getByAccount(sysUserReq.getAccount()).dataOrThrow();
		SaveNewPasswordReq req = new SaveNewPasswordReq();
		req.setSysUserId(sysUserResp.getId());
		// todo: 设置高复杂度密码，并邮件发送
		req.setPassword("12345678");
		sysUserPasswordFeign.saveNewPassword(req).dataOrThrow();
		return Result.success(true);
	}

	@OperationLog(title = "用户信息更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:sysUser:update")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody SysUserReq sysUserReq) {
		// 账号不可修改
		sysUserReq.setAccount(null);
		return sysUserFeign.update(sysUserReq);
	}

	@OperationLog(title = "用户信息删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:sysUser:remove")
	@PostMapping("/remove")
	public Result<Boolean> remove(@RequestBody RemoveReq<Integer> req) {
		return sysUserFeign.remove(req);
	}

	@OperationLog(title = "用户信息导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:sysUser:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		List<SysUserResp> listResp = sysUserFeign.list(account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd).dataOrThrow();
		List<SysUserExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysUserExcel.class);
		ExcelUtil.write2httpResponse(response, "用户信息", SysUserExcel.class, excelList);
	}

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
