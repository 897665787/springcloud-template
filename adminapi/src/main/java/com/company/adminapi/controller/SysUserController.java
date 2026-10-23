package com.company.adminapi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.controller.converter.SysUserIdNicknameConverter;
import com.company.adminapi.converter.annotation.RespConverter;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.excel.SysUserExcel;

import com.company.token.accesscontrol.annotation.RequireLogin;
import com.company.framework.context.HeaderContextUtil;
import com.company.framework.util.PropertyUtils;
import com.company.adminapi.feign.client.SysUserFeign;
import com.company.adminapi.feign.client.SysUserPasswordFeign;
import com.company.adminapi.feign.client.SysUserRoleFeign;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.request.UpdatePasswordReq;
import com.company.system.api.request.SysUserAssignRoleReq;
import com.company.system.api.request.SysUserReq;
import com.company.common.response.PageResp;
import com.company.system.api.response.SysRoleResp;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;

@RequireLogin
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

	@Autowired
	private SysUserFeign sysUserFeign;
	@Autowired
	private SysUserPasswordFeign sysUserPasswordFeign;
	@Autowired
	private SysUserRoleFeign sysUserRoleFeign;
	@Autowired
	private FileController fileController;

	@RespConverter(field = "createBy", newField = "createByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RespConverter(field = "updateBy", newField = "updateByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RequirePermissions("system:user:list")
	@GetMapping("/page")
	public PageResp<com.company.adminapi.resp.SysUserResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		PageResp<SysUserResp> pageResp = sysUserFeign.page(current, size, account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
		List<com.company.adminapi.resp.SysUserResp> respList = PropertyUtils.copyArrayProperties(pageResp.getList(), com.company.adminapi.resp.SysUserResp.class);
        return PageResp.of(pageResp.getTotal(), respList);
	}

	@RespConverter(field = "createBy", newField = "createByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RespConverter(field = "updateBy", newField = "updateByNickname", dataSource = SysUserIdNicknameConverter.class)
	@RequirePermissions("system:user:list")
	@GetMapping("/query")
	public com.company.adminapi.resp.SysUserResp query(@NotNull Integer id) {
		SysUserResp sysUserResp = sysUserFeign.query(id);
		com.company.adminapi.resp.SysUserResp resp = PropertyUtils.copyProperties(sysUserResp, com.company.adminapi.resp.SysUserResp.class);
		return resp;
	}

	@OperationLog(title = "用户信息保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:user:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysUserReq sysUserReq) {
		sysUserFeign.save(sysUserReq);
		SysUserResp sysUserResp = sysUserFeign.getByAccount(sysUserReq.getAccount());
		// 初始密码取参数配置 sys.user.initPassword，不写死在代码里
		SaveNewPasswordReq req = new SaveNewPasswordReq();
		req.setSysUserId(sysUserResp.getId());
		sysUserPasswordFeign.resetPassword(req);
		return true;
	}

	@OperationLog(title = "用户信息更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:user:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysUserReq sysUserReq) {
		// 账号不可修改
		sysUserReq.setAccount(null);
		return sysUserFeign.update(sysUserReq);
	}

	@OperationLog(title = "用户信息删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:user:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysUserFeign.remove(req);
	}

	@OperationLog(title = "用户信息导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:user:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		List<SysUserResp> listResp = sysUserFeign.list(account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);
		List<SysUserExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysUserExcel.class);
		ExcelUtil.write2httpResponse(response, "用户信息", SysUserExcel.class, excelList);
	}

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
	@GetMapping(value = "/getInfo")
	public SysUserInfoResp getInfo() {
		Integer userId = HeaderContextUtil.currentUserIdInt();
		return sysUserFeign.getInfo(userId);
	}

	/**
	 * 个人中心-获取用户信息
	 *
	 * @return 用户信息
	 */
	@GetMapping(value = "/profile")
	public SysUserInfoResp profile() {
		Integer userId = HeaderContextUtil.currentUserIdInt();
		return sysUserFeign.getInfo(userId);
	}

	/**
	 * 个人中心-修改用户信息
	 *
	 * @return 用户信息
	 */
	@PostMapping(value = "/updateProfile")
	public SysUserInfoResp updateProfile(@RequestBody SysUserReq sysUserReq) {
		Integer userId = HeaderContextUtil.currentUserIdInt();
		sysUserFeign.updateProfile(userId, sysUserReq);
		return sysUserFeign.getInfo(userId);
	}

	/**
	 * 个人中心-修改密码
	 *
	 * @return 用户信息
	 */
	@PostMapping(value = "/updatePassword")
	public SysUserInfoResp updatePassword(@RequestBody UpdatePasswordReq updatePasswordReq) {
		Integer userId = HeaderContextUtil.currentUserIdInt();
		sysUserPasswordFeign.changePassword(userId, updatePasswordReq);
		return sysUserFeign.getInfo(userId);
	}

	/**
	 * 个人中心-头像上传
	 *
	 * @param file 头像文件
	 * @return 头像地址
	 */
	@PostMapping(value = "/profile/avatar")
	public Map<String, String> uploadAvatar(@RequestParam("avatarfile") MultipartFile file) {
		Integer userId = HeaderContextUtil.currentUserIdInt();
		Map<String, String> uploadResult = fileController.upload(file);
		String fileKey = uploadResult.get("value");

		SysUserReq sysUserReq = new SysUserReq();
		sysUserReq.setAvatar(fileKey);
		sysUserFeign.updateProfile(userId, sysUserReq);

		// 前端会拼接 VUE_APP_BASE_API 前缀，因此返回站内相对地址，由后端 302 到预签名地址
		Map<String, String> result = new HashMap<>();
		result.put("imgUrl", "/file/download?fileKey=" + fileKey);
		result.put("fileKey", fileKey);
		return result;
	}

	/**
	 * 用户授权角色-查询（返回全部角色，已授权的角色 flag=true）
	 *
	 * @param userId 用户id
	 * @return 用户信息 + 角色列表
	 */
	@GetMapping(value = "/authRole")
	public Map<String, Object> getAuthRole(@NotNull Integer userId) {
		com.company.system.api.response.SysUserResp sysUserResp = sysUserFeign.query(userId);
		List<SysRoleResp> roleList = sysUserFeign.getAuthRole(userId);
		Set<Integer> checkedRoleIds = sysUserRoleFeign.listRoleIdByUserId(userId);

		List<Map<String, Object>> roles = new ArrayList<>();
		roleList.forEach(role -> {
			Map<String, Object> roleView = new HashMap<>();
			roleView.put("roleId", role.getId());
			roleView.put("roleName", role.getRoleName());
			roleView.put("roleKey", role.getRoleKey());
			roleView.put("status", role.getStatus());
			roleView.put("createTime", role.getCreateTime());
			roleView.put("flag", checkedRoleIds.contains(role.getId()));
			roles.add(roleView);
		});

		Map<String, Object> result = new HashMap<>();
		result.put("user", sysUserResp);
		result.put("roles", roles);
		return result;
	}

	/**
	 * 用户授权角色-保存
	 *
	 * @param userId  用户id
	 * @param roleIds 角色id列表
	 * @return 是否成功
	 */
	@OperationLog(title = "用户授权角色", businessType = BusinessType.GRANT)
	@RequirePermissions("system:user:edit")
	@PostMapping(value = "/authRole")
	public Boolean updateAuthRole(@RequestParam("userId") Integer userId, @RequestBody AuthRoleReq req) {
		SysUserAssignRoleReq assignRoleReq = new SysUserAssignRoleReq();
		assignRoleReq.setSysUserId(userId);
		assignRoleReq.setSysRoleIds(req.getRoleIds());
		return sysUserFeign.assignRole(assignRoleReq);
	}

	/**
	 * 用户授权角色请求
	 */
	@Data
	public static class AuthRoleReq {
		/**
		 * 角色id列表
		 */
		private Set<Integer> roleIds;
	}

	/**
	 * 用户密码重置
	 *
	 * @param resetPwdReq 用户id + 新密码
	 * @return 是否成功
	 */
	@OperationLog(title = "用户密码重置", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:user:edit")
	@PostMapping(value = "/resetPwd")
	public Boolean resetPwd(@RequestBody ResetPwdReq resetPwdReq) {
		SaveNewPasswordReq req = new SaveNewPasswordReq();
		req.setSysUserId(resetPwdReq.getSysUserId());
		req.setPassword(resetPwdReq.getPassword());
		return sysUserPasswordFeign.resetPassword(req);
	}

	/**
	 * 用户密码重置请求
	 */
	@Data
	public static class ResetPwdReq {
		/**
		 * 用户id
		 */
		private Integer sysUserId;

		/**
		 * 新密码（明文，为空时使用初始密码）
		 */
		private String password;
	}
}