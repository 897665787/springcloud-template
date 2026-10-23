package com.company.adminapi.controller;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.constants.Constants;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;
import com.company.adminapi.excel.SysConfigExcel;

import com.company.common.response.PageResp;
import com.company.token.accesscontrol.annotation.RequireLogin;
import com.company.system.api.request.RemoveReq;
import com.company.framework.util.PropertyUtils;
import com.company.adminapi.feign.client.SysConfigFeign;
import com.company.adminapi.service.CacheRefreshService;
import com.company.system.api.request.SysConfigReq;
import com.company.system.api.response.SysConfigResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

	@Autowired
	private SysConfigFeign sysConfigFeign;
	@Autowired
	private CacheRefreshService cacheRefreshService;

	@RequirePermissions("system:config:list")
	@GetMapping("/page")
	public PageResp<SysConfigResp> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, String name, String code, String value, String configRemark) {
		return sysConfigFeign.page(current, size, name, code, value, configRemark);
	}

	@RequirePermissions("system:config:list")
	@GetMapping("/query")
	public SysConfigResp query(@NotNull Integer id) {
		return sysConfigFeign.query(id);
	}

    @OperationLog(title = "参数配置保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:config:add")
	@PostMapping("/save")
	public Boolean save(@RequestBody SysConfigReq sysConfigReq) {
		return sysConfigFeign.save(sysConfigReq);
	}

    @OperationLog(title = "参数配置更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:config:edit")
	@PostMapping("/update")
	public Boolean update(@RequestBody SysConfigReq sysConfigReq) {
		return sysConfigFeign.update(sysConfigReq);
	}

    @OperationLog(title = "参数配置删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:config:remove")
	@PostMapping("/remove")
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		return sysConfigFeign.remove(req);
	}

	/**
	 * 刷新参数缓存
	 */
	@OperationLog(title = "参数配置刷新缓存", businessType = BusinessType.CLEAN)
	@RequirePermissions("system:config:remove")
	@PostMapping("/refreshCache")
	public Boolean refreshCache() {
		return cacheRefreshService.refresh(Constants.CacheName.SYS_CONFIG);
	}

	@OperationLog(title = "参数配置导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:config:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, String name, String code, String value, String configRemark) {
		List<SysConfigResp> listResp = sysConfigFeign.list(name, code, value, configRemark);
		List<SysConfigExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysConfigExcel.class);
		ExcelUtil.write2httpResponse(response, "参数配置", SysConfigExcel.class, excelList);
	}


	/**
	 * 根据参数编码查询参数值
	 *
	 * @param code 参数编码
	 * @return 参数值
	 */
	// 参数值查询供各页面读取业务配置（如初始密码提示），仅要求登录
	@RequireLogin
	@GetMapping("/getValueByCode")
	public Map<String, String> getValueByCode(@NotNull String code) {
		return sysConfigFeign.getValueByCode(code);
	}
}
