package com.company.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;

import com.company.system.api.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.constant.Constants;
import com.company.system.api.interfaces.SysUserApi;
import com.company.system.api.request.SysUserAssignRoleReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.SysRoleResp;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;
import com.company.system.entity.SysDept;
import com.company.system.entity.SysMenu;
import com.company.system.entity.SysRole;
import com.company.system.entity.SysUser;
import com.company.system.entity.SysUserPassword;
import com.company.system.entity.SysUserRole;
import com.company.system.service.SysConfigService;
import com.company.system.service.SysDeptService;
import com.company.system.service.SysMenuService;
import com.company.system.service.SysRoleService;
import com.company.system.service.SysUserPasswordService;
import com.company.system.service.SysUserRoleService;
import com.company.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.crypto.SecureUtil;

@RestController
@RequestMapping("/sysUser")
public class SysUserController implements SysUserApi {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserPasswordService sysUserPasswordService;
	@Autowired
	private SysConfigService sysConfigService;

	// 初始密码配置项编码
	private static final String DEFAULT_PASSWORD_CODE = "sys.user.initPassword";

	private QueryWrapper<SysUser> toQueryWrapper(String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(account)) {
			queryWrapper.like("account", account);
		}
		if (StringUtils.isNotBlank(nickname)) {
			queryWrapper.like("nickname", nickname);
		}
		if (StringUtils.isNotBlank(email)) {
			queryWrapper.like("email", email);
		}
		if (StringUtils.isNotBlank(phonenumber)) {
			queryWrapper.like("phonenumber", phonenumber);
		}
        if (StringUtils.isNotBlank(sex)) {
            queryWrapper.eq("sex", sex);
        }
		if (StringUtils.isNotBlank(avatar)) {
			queryWrapper.like("avatar", avatar);
		}
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }
		if (deptId != null) {
			queryWrapper.eq("dept_id", deptId);
		}
		if (StringUtils.isNotBlank(userRemark)) {
			queryWrapper.like("user_remark", userRemark);
		}
        if (StringUtils.isNotBlank(createTimeStart)) {
            queryWrapper.ge("create_time", createTimeStart + " 00:00:00");
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            queryWrapper.le("create_time", createTimeEnd + " 23:59:59");
        }
        if (StringUtils.isNotBlank(updateTimeStart)) {
            queryWrapper.ge("update_time", updateTimeStart + " 00:00:00");
        }
        if (StringUtils.isNotBlank(updateTimeEnd)) {
            queryWrapper.le("update_time", updateTimeEnd + " 23:59:59");
        }
		return queryWrapper;
	}

	@Override
	public PageResp<SysUserResp> page(Long current, Long size, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUser> queryWrapper = toQueryWrapper(account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);

		long count = sysUserService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysUser> list = sysUserService.list(PageDTO.of(current, size), queryWrapper);

		List<SysUserResp> respList = PropertyUtils.copyArrayProperties(list, SysUserResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public List<SysUserResp> list(String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
		QueryWrapper<SysUser> queryWrapper = toQueryWrapper(account, nickname, email, phonenumber, sex, avatar, status, deptId, userRemark, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd);

		queryWrapper.orderByDesc("id");
		List<SysUser> list = sysUserService.list(queryWrapper);

		List<SysUserResp> respList = PropertyUtils.copyArrayProperties(list, SysUserResp.class);
		return respList;
	}

	@Override
	public SysUserResp query(Integer id) {
		SysUser sysUser = sysUserService.getById(id);
		SysUserResp sysUserResp = PropertyUtils.copyProperties(sysUser, SysUserResp.class);
		return sysUserResp;
	}

	@Override
	public Boolean save(SysUserReq sysUserReq) {
		SysUser sysUser = PropertyUtils.copyProperties(sysUserReq, SysUser.class);
		boolean success = sysUserService.save(sysUser);
		return success;
	}

	@Override
	public Boolean update(SysUserReq sysUserReq) {
		SysUser sysUser = PropertyUtils.copyProperties(sysUserReq, SysUser.class);
		boolean success = sysUserService.updateById(sysUser);
		return success;
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return true;
		}
		boolean success = sysUserService.removeByIds(req.getIdList());
		return success;
	}

	@Override
	public SysUserResp getByAccount(String account) {
		SysUser sysUser = sysUserService.getByAccount(account);
		return PropertyUtils.copyProperties(sysUser, SysUserResp.class);
	}

	@Override
	public SysUserResp getById(Integer id) {
		SysUser sysUser = sysUserService.getById(id);
		return PropertyUtils.copyProperties(sysUser, SysUserResp.class);
	}

	@Override
	public SysUserInfoResp getInfo(Integer userId) {
		SysUser user = sysUserService.getById(userId);
		Set<String> permissions = new HashSet<>();
		boolean isAdmin = sysRoleService.hasRole(userId, Constants.SUPER_ROLE);
		if (isAdmin) {
			permissions.add("*:*:*");
		} else {
            List<SysMenu> menuList = sysMenuService.getByUserId(userId);
			menuList.forEach(v -> {
				if (StringUtils.isNotBlank(v.getPerms())) {
					permissions.addAll(Arrays.asList(v.getPerms().trim().split(",")));
				}
			});
		}

		SysUserInfoResp.User userInfo = PropertyUtils.copyProperties(user, SysUserInfoResp.User.class);
		if (user != null && user.getDeptId() != null) {
			SysDept dept = sysDeptService.getById(user.getDeptId());
			if (dept != null) {
				userInfo.setDeptName(dept.getName());
			}
		}
		if (user != null) {
			userInfo.setCreateTime(user.getCreateTime());
		}

		SysUserInfoResp resp = new SysUserInfoResp().setUser(userInfo)
				.setPermissions(permissions)
				.setRoles(listRoleKeyByUserId(userId))
				.setIsDefaultModifyPwd(isDefaultPassword(userId))
				.setIsPasswordExpired(isPasswordExpired(userId));
		return resp;
	}

	@Override
	public Boolean updateProfile(Integer userId, SysUserReq sysUserReq) {
		// 只允许修改个人资料字段，避免越权修改账号、状态、部门
		SysUser sysUser = new SysUser();
		sysUser.setId(userId);
		sysUser.setNickname(sysUserReq.getNickname());
		sysUser.setEmail(sysUserReq.getEmail());
		sysUser.setPhonenumber(sysUserReq.getPhonenumber());
		sysUser.setSex(sysUserReq.getSex());
		sysUser.setAvatar(sysUserReq.getAvatar());
		return sysUserService.updateById(sysUser);
	}

	@Override
	public PageResp<SysUserResp> pageAllocatedByRole(Long current, Long size, Integer roleId, String account, String nickname, String phonenumber) {
		List<SysUserRole> assignedList = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("role_id", roleId));
		Set<Integer> assignedUserIds = assignedList.stream().map(SysUserRole::getUserId).collect(Collectors.toSet());
		if (assignedUserIds.isEmpty()) {
			return PageResp.of(0L, new ArrayList<>());
		}

		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", assignedUserIds);
		if (StringUtils.isNotBlank(account)) {
			queryWrapper.like("account", account);
		}
		if (StringUtils.isNotBlank(nickname)) {
			queryWrapper.like("nickname", nickname);
		}
		if (StringUtils.isNotBlank(phonenumber)) {
			queryWrapper.like("phonenumber", phonenumber);
		}
		long count = sysUserService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysUser> list = sysUserService.list(PageDTO.of(current, size), queryWrapper);
		List<SysUserResp> respList = PropertyUtils.copyArrayProperties(list, SysUserResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public PageResp<SysUserResp> pageUnallocatedByRole(Long current, Long size, Integer roleId, String account, String nickname, String phonenumber) {
		// 已分配该角色的用户id
		List<SysUserRole> assignedList = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("role_id", roleId));
		Set<Integer> assignedUserIds = assignedList.stream().map(SysUserRole::getUserId).collect(Collectors.toSet());

		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(account)) {
			queryWrapper.like("account", account);
		}
		if (StringUtils.isNotBlank(nickname)) {
			queryWrapper.like("nickname", nickname);
		}
		if (StringUtils.isNotBlank(phonenumber)) {
			queryWrapper.like("phonenumber", phonenumber);
		}
		if (!assignedUserIds.isEmpty()) {
			queryWrapper.notIn("id", assignedUserIds);
		}
		long count = sysUserService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysUser> list = sysUserService.list(PageDTO.of(current, size), queryWrapper);
		List<SysUserResp> respList = PropertyUtils.copyArrayProperties(list, SysUserResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public List<SysRoleResp> getAuthRole(Integer userId) {
		// 返回全部角色，由 adminapi 标记该用户已授权的角色
		List<SysRole> roleList = sysRoleService.list();
		return PropertyUtils.copyArrayProperties(roleList, SysRoleResp.class);
	}

	/**
	 * 查询用户的角色权限字符串
	 */
	private Set<String> listRoleKeyByUserId(Integer userId) {
		Set<Integer> roleIds = sysUserRoleService.listRoleIdByUserId(userId);
		if (roleIds.isEmpty()) {
			return new HashSet<>();
		}
		return sysRoleService.listByIds(roleIds).stream()
				.map(SysRole::getRoleKey)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toSet());
	}

	/**
	 * 是否仍在使用初始密码
	 */
	private boolean isDefaultPassword(Integer userId) {
		SysUserPassword sysUserPassword = sysUserPasswordService.getLastBySysUserId(userId);
		if (sysUserPassword == null) {
			return false;
		}
		String defaultPassword = sysConfigService.getValueByCode(DEFAULT_PASSWORD_CODE);
		if (StringUtils.isBlank(defaultPassword)) {
			return false;
		}
		String md5Password = SecureUtil.md5(defaultPassword);
		return md5Password.equals(sysUserPassword.getPassword());
	}

	/**
	 * 密码是否已过期
	 */
	private boolean isPasswordExpired(Integer userId) {
		SysUserPassword sysUserPassword = sysUserPasswordService.getLastBySysUserId(userId);
		if (sysUserPassword == null || sysUserPassword.getExpireTime() == null) {
			return false;
		}
		return sysUserPassword.getExpireTime().compareTo(LocalDateTime.now()) <= 0;
	}

	@Override
	public List<SysUserResp> getByBatchId(@RequestBody List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}
		List<SysUser> sysUsers = sysUserService.listByIds(ids);
		return PropertyUtils.copyArrayProperties(sysUsers, SysUserResp.class);
	}

	@Override
	public Map<Integer, String> mapNicknameById(Collection<Integer> ids) {
		List<SysUser> sysUserList = sysUserService.selectByIds(ids);
		Map<Integer, String> idNicknameMap = sysUserList.stream()
				.collect(Collectors.toMap(SysUser::getId, SysUser::getNickname));
		return idNicknameMap;
	}

	@Override
	public Boolean assignRole(@RequestBody SysUserAssignRoleReq req) {
		sysUserRoleService.assignRole(req.getSysUserId(), req.getSysRoleIds());
		return true;
	}
}