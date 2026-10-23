package com.company.adminapi.controller;

/**
 * 用户导入（兼容若依前端导入组件）
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.company.adminapi.excel.SysUserImportExcel;
import com.company.adminapi.feign.client.SysUserFeign;
import com.company.adminapi.feign.client.SysUserPasswordFeign;
import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.SysUserResp;
import com.company.token.accesscontrol.annotation.RequireLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feiniaojin.gracefulresponse.api.ExcludeFromGracefulResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户导入（兼容若依前端导入组件）
 * <p>
 * 若依用户列表页的上传地址固定为 /system/user/importData，这里按同一路径提供导入能力，
 * 返回 {code:200,msg:...} 供前端弹出导入结果。
 */
@RequireLogin
@Slf4j
@RestController
@RequestMapping("/system/user")
public class SysUserImportController {

	// 前端约定的成功码
	private static final String SUCCESS_CODE = "200";

	// 是否更新已存在用户
	private static final int UPDATE_SUPPORT = 1;

	// 导入模板中字典 sys_normal_disable 的取值：0-正常
	private static final String DICT_STATUS_NORMAL = "0";

	// 后端用户状态：正常
	private static final String USER_STATUS_ON = "ON";

	// 后端用户状态：停用
	private static final String USER_STATUS_OFF = "OFF";

	@Autowired
	private SysUserFeign sysUserFeign;
	@Autowired
	private SysUserPasswordFeign sysUserPasswordFeign;
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 下载用户导入模板
	 */
	@ExcludeFromGracefulResponse
	@GetMapping("/importTemplate")
	public void importTemplate(HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=user_template.xlsx");
		try (OutputStream outputStream = response.getOutputStream()) {
			EasyExcel.write(outputStream, SysUserImportExcel.class).excelType(ExcelTypeEnum.XLSX).sheet("用户数据")
					.doWrite(Collections.emptyList());
		}
	}

	/**
	 * 导入用户数据
	 *
	 * @param file          用户数据文件
	 * @param updateSupport 是否更新已存在的用户
	 * @return 导入结果
	 */
	@ExcludeFromGracefulResponse
	@PostMapping("/importData")
	public void importData(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "updateSupport", required = false) Integer updateSupport, HttpServletResponse response)
			throws IOException {
		StringBuilder message = new StringBuilder();
		if (file == null || file.getSize() == 0) {
			writeResult(response, "1", "请选择要导入的文件");
			return;
		}

		List<SysUserImportExcel> rows;
		try {
			rows = EasyExcel.read(file.getInputStream()).head(SysUserImportExcel.class).sheet().doReadSync();
		} catch (Exception e) {
			log.error("读取用户导入文件失败", e);
			writeResult(response, "1", "读取文件失败:" + e.getMessage());
			return;
		}

		boolean allowUpdate = updateSupport != null && updateSupport == UPDATE_SUPPORT;
		List<String> successList = new ArrayList<>();
		List<String> skipList = new ArrayList<>();
		List<String> failList = new ArrayList<>();

		for (SysUserImportExcel row : rows) {
			String account = StringUtils.trimToEmpty(row.getAccount());
			if (StringUtils.isBlank(account)) {
				continue;
			}
			SysUserResp exist = sysUserFeign.getByAccount(account);
			try {
				if (exist == null) {
					createUser(row, account);
					successList.add(account);
				} else if (allowUpdate) {
					updateUser(exist.getId(), row);
					successList.add(account);
				} else {
					skipList.add(account);
				}
			} catch (Exception e) {
				log.error("导入用户失败,account:{}", account, e);
				failList.add(account + "(" + e.getMessage() + ")");
			}
		}

		message.append("导入完成：成功 ").append(successList.size()).append(" 条");
		if (!skipList.isEmpty()) {
			message.append("，已存在跳过 ").append(skipList.size()).append(" 条");
		}
		if (!failList.isEmpty()) {
			message.append("，失败 ").append(failList.size()).append(" 条：").append(String.join("、", failList));
		}
		writeResult(response, SUCCESS_CODE, message.toString());
	}

	/**
	 * 新建用户，初始密码取参数配置
	 */
	private void createUser(SysUserImportExcel row, String account) {
		SysUserReq req = toUserReq(row);
		req.setAccount(account);
		sysUserFeign.save(req);

		SysUserResp saved = sysUserFeign.getByAccount(account);
		if (saved != null) {
			SaveNewPasswordReq passwordReq = new SaveNewPasswordReq();
			passwordReq.setSysUserId(saved.getId());
			// 密码为空时使用配置项 sys.user.initPassword
			sysUserPasswordFeign.resetPassword(passwordReq);
		}
	}

	/**
	 * 更新已存在用户
	 */
	private void updateUser(Integer userId, SysUserImportExcel row) {
		SysUserReq req = toUserReq(row);
		req.setId(userId);
		req.setAccount(null);
		sysUserFeign.update(req);
	}

	/**
	 * 导入行 -> 用户请求
	 */
	private SysUserReq toUserReq(SysUserImportExcel row) {
		SysUserReq req = new SysUserReq();
		req.setNickname(StringUtils.trimToNull(row.getNickname()));
		req.setDeptId(row.getDeptId());
		req.setPhonenumber(StringUtils.trimToNull(row.getPhonenumber()));
		req.setEmail(StringUtils.trimToNull(row.getEmail()));
		req.setSex(StringUtils.trimToNull(row.getSex()));
		req.setStatus(toServerStatus(row.getStatus()));
		return req;
	}

	/**
	 * 导入模板中的状态值（字典 sys_normal_disable：0正常/1停用）-> 后端状态值（ON/OFF）
	 */
	private String toServerStatus(String status) {
		if (StringUtils.isBlank(status)) {
			return null;
		}
		return DICT_STATUS_NORMAL.equals(status.trim()) ? USER_STATUS_ON : USER_STATUS_OFF;
	}

	/**
	 * 输出导入结果
	 */
	private void writeResult(HttpServletResponse response, String code, String msg) throws IOException {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("code", code);
		result.put("msg", msg);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		try (OutputStream outputStream = response.getOutputStream()) {
			outputStream.write(objectMapper.writeValueAsString(result).getBytes("UTF-8"));
		}
	}
}
