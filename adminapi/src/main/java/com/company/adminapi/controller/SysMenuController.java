package com.company.adminapi.controller;

import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.annotation.RequirePermissions;
import com.company.adminapi.easyexcel.ExcelUtil;
import com.company.adminapi.excel.SysMenuExcel;
import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.framework.annotation.RequireLogin;
import com.company.system.api.feign.SysMenuFeign;
import com.company.system.api.request.SysMenuReq;
import com.company.system.api.response.RouterResp;
import com.company.system.api.response.SysMenuResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.company.adminapi.enums.OperationLogEnum.BusinessType;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

	@Autowired
	private SysMenuFeign sysMenuFeign;

	@RequirePermissions("system:sysMenu:query")
	@GetMapping("/page")
	public Result<PageResp<SysMenuResp>> page(@NotNull @Min(value = 1) Long current, @NotNull Long size, Integer parentId,
											  String menuName, String menuType, String status, Integer visible, String perms,
											  String createTimeStart, String createTimeEnd) {
		return sysMenuFeign.page(current, size, parentId, menuName, menuType, status, visible, perms, createTimeStart, createTimeEnd);
	}

	@RequirePermissions("system:sysMenu:query")
	@GetMapping("/tree")
	public Result<List<SysMenuResp>> tree(Integer parentId, String menuName, Integer orderNum, String menuType,
										  String status, Integer visible, String perms,
										  String createTimeStart, String createTimeEnd) {
		List<SysMenuResp> sysMenuResps = sysMenuFeign.list(parentId, menuName, orderNum, menuType, status, visible, perms,
				createTimeStart, createTimeEnd).dataOrThrow();
		Map<Integer, SysMenuResp> menuMap = sysMenuResps.stream().collect(Collectors.toMap(SysMenuResp::getId, Function.identity()));
		Set<Integer> childrenId = new HashSet<>();
		sysMenuResps.forEach(menu -> {
			if (menu.getParentId() == 0) {
				return;
			}
			SysMenuResp parent = menuMap.get(menu.getParentId());
			if (parent != null) {
				if (parent.getChildrenList() == null) {
					parent.setChildrenList(new ArrayList<>());
				}
				parent.getChildrenList().add(menu);
				childrenId.add(menu.getId());
			}
		});
		List<SysMenuResp> rootList = sysMenuResps.stream().filter(m -> !childrenId.contains(m.getId())).collect(Collectors.toList());
		return Result.success(rootList);
	}

	@RequirePermissions("system:sysMenu:query")
	@GetMapping("/query")
	public Result<SysMenuResp> query(@NotNull Integer id) {
		return sysMenuFeign.query(id);
	}

	@OperationLog(title = "菜单权限保存", businessType = BusinessType.INSERT)
	@RequirePermissions("system:sysMenu:save")
	@PostMapping("/save")
	public Result<Boolean> save(@RequestBody SysMenuReq sysMenuReq) {
		return sysMenuFeign.save(sysMenuReq);
	}

	@OperationLog(title = "菜单权限更新", businessType = BusinessType.UPDATE)
	@RequirePermissions("system:sysMenu:update")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody SysMenuReq sysMenuReq) {
		return sysMenuFeign.update(sysMenuReq);
	}

	@OperationLog(title = "菜单权限删除", businessType = BusinessType.DELETE)
	@RequirePermissions("system:sysMenu:remove")
	@PostMapping("/remove")
	public Result<Boolean> remove(@RequestBody RemoveReq<Integer> req) {
		return sysMenuFeign.remove(req);
	}

	@OperationLog(title = "菜单权限导出", businessType = BusinessType.EXPORT, isSaveResponseData = false)
	@RequirePermissions("system:sysMenu:export")
	@GetMapping("/export")
	public void export(HttpServletResponse response, Integer parentId, String menuName, Integer orderNum, String menuType,
					   String status, Integer visible, String perms, String createTimeStart, String createTimeEnd) {
		List<SysMenuResp> listResp = sysMenuFeign.list(parentId, menuName, orderNum, menuType, status, visible, perms,
				createTimeStart, createTimeEnd).dataOrThrow();
		List<SysMenuExcel> excelList = PropertyUtils.copyArrayProperties(listResp, SysMenuExcel.class);
		ExcelUtil.write2httpResponse(response, "菜单权限", SysMenuExcel.class, excelList);
	}

	@RequireLogin
	@GetMapping(value = "/getRouters")
	public Result<List<RouterResp>> getRouters() {
		return Result.success();
	}

}