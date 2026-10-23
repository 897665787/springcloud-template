package com.company.codegenerate.service;

/**
 * 代码模板渲染服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.codegenerate.entity.GenTable;
import com.company.codegenerate.entity.GenTableColumn;
import com.company.framework.globalresponse.ExceptionUtil;

/**
 * 代码模板渲染服务
 * <p>
 * 复用 code-generate 已有的 velocity 模板，把渲染结果返回给前端预览或打包下载。
 */
@Service
public class GenTemplateService {

	// 模板路径前缀（classpath）
	private static final String TEMPLATE_PREFIX = "templates/";

	// 模板文件后缀
	private static final String TEMPLATE_SUFFIX = ".vm";

	// 代码包层级
	private static final String PACKAGE_ENTITY = "entity";
	private static final String PACKAGE_MAPPER = "mapper";
	private static final String PACKAGE_SERVICE = "service";
	private static final String PACKAGE_CONTROLLER = "controller";
	private static final String PACKAGE_REQUEST = "request";
	private static final String PACKAGE_RESPONSE = "response";
	private static final String PACKAGE_INTERFACES = "interfaces";
	private static final String PACKAGE_FEIGN = "feign.client";
	private static final String PACKAGE_EXCEL = "excel";

	// 模板文件名 -> 预览标签名（前端按 / 后、.vm 前截取标签，中文语言按第一个 . 后截取）
	private static final Map<String, String> PREVIEW_KEY = new LinkedHashMap<>();

	static {
		PREVIEW_KEY.put("entity.java.vm", "vm/java/domain.java.vm");
		PREVIEW_KEY.put("entityReq.java.vm", "vm/java/domainReq.java.vm");
		PREVIEW_KEY.put("entityResp.java.vm", "vm/java/domainResp.java.vm");
		PREVIEW_KEY.put("api.java.vm", "vm/java/api.java.vm");
		PREVIEW_KEY.put("mapper.java.vm", "vm/java/mapper.java.vm");
		PREVIEW_KEY.put("mapper.xml.vm", "vm/xml/mapper.xml.vm");
		PREVIEW_KEY.put("service.java.vm", "vm/java/service.java.vm");
		PREVIEW_KEY.put("controller.java.vm", "vm/java/controller.java.vm");
		PREVIEW_KEY.put("adminapi-controller.java.vm", "vm/java/adminapiController.java.vm");
		PREVIEW_KEY.put("adminapi-feign.java.vm", "vm/java/adminapiFeign.java.vm");
		PREVIEW_KEY.put("adminapi-entityExcel.java.vm", "vm/java/excel.java.vm");
	}

	@Autowired
	private GenTableService genTableService;

	// velocity 引擎
	private final VelocityEngine velocityEngine;

	public GenTemplateService() {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty("resource.loader", "class");
		engine.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.setProperty("input.encoding", "UTF-8");
		engine.setProperty("output.encoding", "UTF-8");
		engine.init();
		this.velocityEngine = engine;
	}

	/**
	 * 预览代码：key 为预览文件名（与若依前端标签对齐），value 为渲染结果
	 */
	public Map<String, String> preview(Integer tableId) {
		GenTable genTable = genTableService.getByTableId(tableId);
		if (genTable == null) {
			ExceptionUtil.throwException("生成配置不存在");
		}
		Map<String, String> result = new LinkedHashMap<>();
		renderTable(genTable).forEach((templateName, content) -> {
			result.put(PREVIEW_KEY.getOrDefault(templateName, templateName), content);
		});
		return result;
	}

	/**
	 * 生成代码压缩包内容：key 为压缩包内路径，value 为文件内容
	 */
	public Map<String, String> files(String tableNames) {
		Map<String, String> result = new LinkedHashMap<>();
		if (StringUtils.isBlank(tableNames)) {
			ExceptionUtil.throwException("请选择要生成的表");
		}
		for (String item : tableNames.split(",")) {
			String tableName = item.trim();
			if (StringUtils.isBlank(tableName)) {
				continue;
			}
			GenTable genTable = genTableService.getByTableName(tableName);
			if (genTable == null) {
				ExceptionUtil.throwException("表[" + tableName + "]尚未导入生成配置");
			}
			renderTable(genTable).forEach((templateName, content) -> {
				result.put(buildRelativePath(templateName, genTable), content);
			});
		}
		return result;
	}

	/**
	 * 生成代码压缩包
	 */
	public byte[] zip(String tableNames) {
		Map<String, String> files = this.files(tableNames);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
			for (Map.Entry<String, String> entry : files.entrySet()) {
				zipOutputStream.putNextEntry(new ZipEntry(entry.getKey()));
				zipOutputStream.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
				zipOutputStream.closeEntry();
			}
		} catch (Exception e) {
			ExceptionUtil.throwException("生成代码压缩包失败:" + e.getMessage());
		}
		return outputStream.toByteArray();
	}

	/**
	 * 渲染单张表的全部模板
	 */
	private Map<String, String> renderTable(GenTable genTable) {
		List<GenTableColumn> columns = genTableService.listColumn(genTable.getId());
		Map<String, String> result = new LinkedHashMap<>();
		for (String templateName : templateNames()) {
			result.put(templateName, render(templateName, genTable, columns));
		}
		return result;
	}

	/**
	 * 参与生成的模板列表
	 */
	private List<String> templateNames() {
		List<String> names = new ArrayList<>();
		names.add("entity.java.vm");
		names.add("entityReq.java.vm");
		names.add("entityResp.java.vm");
		names.add("api.java.vm");
		names.add("mapper.java.vm");
		names.add("mapper.xml.vm");
		names.add("service.java.vm");
		names.add("controller.java.vm");
		names.add("adminapi-controller.java.vm");
		names.add("adminapi-feign.java.vm");
		names.add("adminapi-entityExcel.java.vm");
		return names;
	}

	/**
	 * 渲染单个模板
	 */
	private String render(String templateName, GenTable genTable, List<GenTableColumn> columns) {
		VelocityContext context = buildContext(genTable, columns);
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.getTemplate(TEMPLATE_PREFIX + templateName, "UTF-8").merge(context, writer);
		} catch (Exception e) {
			return "模板渲染失败: " + e.getMessage();
		}
		return writer.toString();
	}

	/**
	 * 构建模板上下文
	 */
	private VelocityContext buildContext(GenTable genTable, List<GenTableColumn> columns) {
		VelocityContext context = new VelocityContext();
		context.put("table", buildTable(genTable, columns));
		context.put("package", buildPackage(genTable));
		context.put("columns", buildFields(columns));
		context.put("entity", genTable.getClassName());
		context.put("author", StringUtils.defaultIfBlank(genTable.getFunctionAuthor(), "CodeGenerate"));
		context.put("datetime", java.time.LocalDateTime.now());
		context.put("apiPackage", genTable.getPackageName() + "." + genTable.getModuleName() + ".api");
		context.put("adminapiPackage", genTable.getPackageName() + ".adminapi");
		context.put("moduleName", genTable.getModuleName());
		context.put("_moduleNameUpper", StringUtils.upperCase(genTable.getModuleName()));
		context.put("_entity", lowerFirst(genTable.getClassName()));
		context.put("_serviceName", lowerFirst(genTable.getClassName()) + "Service");
		return context;
	}

	/**
	 * 构建模板中的 table 变量
	 */
	private Map<String, Object> buildTable(GenTable genTable, List<GenTableColumn> columns) {
		String className = genTable.getClassName();
		Map<String, Object> table = new LinkedHashMap<>();
		table.put("name", genTable.getTableName());
		table.put("comment", StringUtils.defaultString(genTable.getTableComment()));
		table.put("entityName", className);
		table.put("mapperName", className + "Mapper");
		table.put("serviceName", className + "Service");
		table.put("controllerName", className + "Controller");
		table.put("fields", buildFields(columns));
		return table;
	}

	/**
	 * 构建模板中的 package 变量
	 */
	private Map<String, Object> buildPackage(GenTable genTable) {
		String base = genTable.getPackageName() + "." + genTable.getModuleName();
		Map<String, Object> packageMap = new LinkedHashMap<>();
		packageMap.put("Entity", base + "." + PACKAGE_ENTITY);
		packageMap.put("Mapper", base + "." + PACKAGE_MAPPER);
		packageMap.put("Service", base + "." + PACKAGE_SERVICE);
		packageMap.put("Controller", base + "." + PACKAGE_CONTROLLER);
		return packageMap;
	}

	/**
	 * 构建模板中的字段变量
	 */
	private List<Map<String, Object>> buildFields(List<GenTableColumn> columns) {
		List<Map<String, Object>> fields = new ArrayList<>();
		for (GenTableColumn column : columns) {
			Map<String, Object> field = new LinkedHashMap<>();
			field.put("name", column.getColumnName());
			field.put("comment", StringUtils.defaultString(column.getColumnComment()));
			field.put("propertyName", column.getJavaField());
			field.put("propertyType", column.getJavaType());
			field.put("columnType", column.getColumnType());
			field.put("isPk", column.getIsPk());
			field.put("isRequired", column.getIsRequired());
			field.put("isInsert", column.getIsInsert());
			field.put("isEdit", column.getIsEdit());
			field.put("isList", column.getIsList());
			field.put("isQuery", column.getIsQuery());
			field.put("queryType", column.getQueryType());
			field.put("htmlType", column.getHtmlType());
			field.put("dictType", column.getDictType());
			field.put("sort", column.getSort());
			fields.add(field);
		}
		return fields;
	}

	/**
	 * 模板文件对应的输出路径
	 */
	private String buildRelativePath(String templateName, GenTable genTable) {
		String moduleName = genTable.getModuleName();
		String packagePath = (genTable.getPackageName() + "." + moduleName).replace(".", "/");
		String apiPackagePath = packagePath + "/api";
		String adminapiPackagePath = genTable.getPackageName().replace(".", "/") + "/adminapi";
		String entity = genTable.getClassName();
		String serviceDir = moduleName + "/service/src/main/java/" + packagePath;
		String apiDir = moduleName + "/api/src/main/java/" + apiPackagePath;
		String adminapiDir = "adminapi/src/main/java/" + adminapiPackagePath;
		switch (templateName) {
			case "entity.java.vm":
				return serviceDir + "/" + PACKAGE_ENTITY + "/" + entity + ".java";
			case "mapper.java.vm":
				return serviceDir + "/" + PACKAGE_MAPPER + "/" + entity + "Mapper.java";
			case "mapper.xml.vm":
				return moduleName + "/service/src/main/resources/mapper/" + entity + "Mapper.xml";
			case "service.java.vm":
				return serviceDir + "/" + PACKAGE_SERVICE + "/" + entity + "Service.java";
			case "controller.java.vm":
				return serviceDir + "/" + PACKAGE_CONTROLLER + "/" + entity + "Controller.java";
			case "entityReq.java.vm":
				return apiDir + "/" + PACKAGE_REQUEST + "/" + entity + "Req.java";
			case "entityResp.java.vm":
				return apiDir + "/" + PACKAGE_RESPONSE + "/" + entity + "Resp.java";
			case "api.java.vm":
				return apiDir + "/" + PACKAGE_INTERFACES + "/" + entity + "Api.java";
			case "adminapi-feign.java.vm":
				return adminapiDir + "/" + PACKAGE_FEIGN + "/" + entity + "Feign.java";
			case "adminapi-controller.java.vm":
				return adminapiDir + "/" + PACKAGE_CONTROLLER + "/" + entity + "Controller.java";
			case "adminapi-entityExcel.java.vm":
				return adminapiDir + "/" + PACKAGE_EXCEL + "/" + entity + "Excel.java";
			default:
				return templateName.replace(TEMPLATE_SUFFIX, "");
		}
	}

	/**
	 * 首字母转小写
	 */
	private String lowerFirst(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		return Character.toLowerCase(value.charAt(0)) + value.substring(1);
	}
}
