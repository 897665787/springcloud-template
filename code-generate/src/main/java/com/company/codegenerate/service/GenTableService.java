package com.company.codegenerate.service;

/**
 * 代码生成配置服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.codegenerate.entity.GenTable;
import com.company.codegenerate.entity.GenTableColumn;
import com.company.codegenerate.mapper.GenTableColumnMapper;
import com.company.codegenerate.mapper.GenTableMapper;
import com.company.framework.globalresponse.ExceptionUtil;
import com.company.system.api.request.SysGenTableColumnReq;
import com.company.system.api.request.SysGenTableReq;

import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成配置服务
 * <p>
 * 维护 sys_gen_table / sys_gen_table_column 配置，并读取数据库元数据。
 */
@Slf4j
@Service
public class GenTableService {

	// 需要忽略的系统表
	private static final List<String> IGNORE_TABLES = Arrays.asList("sys_gen_table", "sys_gen_table_column");

	// 默认单表模板
	public static final String TPL_CRUD = "crud";

	// 主键标识
	private static final String COLUMN_KEY_PRIMARY = "PRI";

	// 自增标识
	private static final String EXTRA_AUTO_INCREMENT = "auto_increment";

	// 数据库表查询语句
	private static final String SQL_DB_TABLE = "SELECT table_name AS tableName, table_comment AS tableComment, "
			+ "create_time AS createTime, update_time AS updateTime "
			+ "FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE'";

	// 数据库字段查询语句
	private static final String SQL_DB_COLUMN = "SELECT column_name AS columnName, column_type AS columnType, "
			+ "column_comment AS columnComment, column_key AS columnKey, extra AS extra "
			+ "FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? "
			+ "ORDER BY ordinal_position";

	// 数据表注释查询语句
	private static final String SQL_TABLE_COMMENT = "SELECT table_comment FROM information_schema.tables "
			+ "WHERE table_schema = DATABASE() AND table_name = ?";

	@Autowired
	private GenTableMapper genTableMapper;
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询已导入的生成配置
	 */
	public List<GenTable> list(String tableName, String tableComment) {
		LambdaQueryWrapper<GenTable> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(tableName)) {
			wrapper.like(GenTable::getTableName, tableName);
		}
		if (StringUtils.isNotBlank(tableComment)) {
			wrapper.like(GenTable::getTableComment, tableComment);
		}
		wrapper.orderByDesc(GenTable::getId);
		return genTableMapper.selectList(wrapper);
	}

	/**
	 * 查询表中的字段配置
	 */
	public List<GenTableColumn> listColumn(Integer tableId) {
		LambdaQueryWrapper<GenTableColumn> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GenTableColumn::getTableId, tableId);
		wrapper.orderByAsc(GenTableColumn::getSort);
		return genTableColumnMapper.selectList(wrapper);
	}

	/**
	 * 查询数据库中可导入的表（排除已导入与系统表）
	 */
	public List<Map<String, Object>> listDbTable(String tableName, String tableComment) {
		Set<String> imported = list(null, null).stream().map(GenTable::getTableName).collect(Collectors.toSet());

		StringBuilder sql = new StringBuilder(SQL_DB_TABLE);
		List<Object> args = new ArrayList<>();
		if (StringUtils.isNotBlank(tableName)) {
			sql.append(" AND table_name LIKE ?");
			args.add("%" + tableName.trim() + "%");
		}
		if (StringUtils.isNotBlank(tableComment)) {
			sql.append(" AND table_comment LIKE ?");
			args.add("%" + tableComment.trim() + "%");
		}
		sql.append(" ORDER BY create_time DESC");

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), args.toArray());
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			String name = String.valueOf(row.get("tableName"));
			if (isIgnore(name) || imported.contains(name)) {
				continue;
			}
			result.add(row);
		}
		return result;
	}

	/**
	 * 导入表：读取结构并生成字段配置
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean importTable(String tables) {
		if (StringUtils.isBlank(tables)) {
			return true;
		}
		for (String item : tables.split(",")) {
			String tableName = item.trim();
			if (StringUtils.isBlank(tableName) || isIgnore(tableName)) {
				continue;
			}
			if (this.getByTableName(tableName) != null) {
				continue;
			}

			GenTable genTable = new GenTable();
			genTable.setTableName(tableName);
			genTable.setTableComment(tableComment(tableName));
			genTable.setClassName(toClassName(tableName));
			genTable.setTplCategory(TPL_CRUD);
			genTable.setPackageName("com.company");
			genTable.setModuleName(toModuleName(tableName));
			genTable.setBusinessName(toClassName(tableName));
			genTable.setFunctionName(genTable.getTableComment());
			genTable.setFunctionAuthor("CodeGenerate");
			genTable.setGenType("0");
			genTableMapper.insert(genTable);

			for (GenTableColumn column : readColumns(tableName)) {
				column.setTableId(genTable.getId());
				genTableColumnMapper.insert(column);
			}
		}
		return true;
	}

	/**
	 * 同步表结构：保留已有配置，仅补充新增字段
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean synchDb(String tableName) {
		GenTable genTable = this.getByTableName(tableName);
		if (genTable == null) {
			return this.importTable(tableName);
		}
		Set<String> existColumns = listColumn(genTable.getId()).stream()
				.map(GenTableColumn::getColumnName).collect(Collectors.toSet());
		for (GenTableColumn column : readColumns(tableName)) {
			if (!existColumns.contains(column.getColumnName())) {
				column.setTableId(genTable.getId());
				genTableColumnMapper.insert(column);
			}
		}
		genTable.setTableComment(tableComment(tableName));
		genTableMapper.updateById(genTable);
		return true;
	}

	/**
	 * 更新生成配置（表信息 + 字段信息）
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean update(SysGenTableReq req) {
		GenTable genTable = this.getByTableId(req.getId());
		if (genTable == null) {
			ExceptionUtil.throwException("生成配置不存在");
		}
		genTable.setTableComment(req.getTableComment());
		genTable.setClassName(req.getClassName());
		genTable.setTplCategory(req.getTplCategory());
		genTable.setPackageName(req.getPackageName());
		genTable.setModuleName(req.getModuleName());
		genTable.setBusinessName(req.getBusinessName());
		genTable.setFunctionName(req.getFunctionName());
		genTable.setFunctionAuthor(req.getFunctionAuthor());
		genTable.setGenType(req.getGenType());
		genTable.setGenPath(req.getGenPath());
		genTable.setRemark(req.getRemark());
		genTableMapper.updateById(genTable);

		if (req.getColumns() != null) {
			req.getColumns().forEach(item -> genTableColumnMapper.updateById(toColumnEntity(item)));
		}
		return true;
	}

	/**
	 * 删除生成配置
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean remove(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return true;
		}
		ids.forEach(id -> {
			LambdaQueryWrapper<GenTableColumn> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(GenTableColumn::getTableId, id);
			genTableColumnMapper.delete(wrapper);
		});
		return genTableMapper.deleteBatchIds(ids) > 0;
	}

	/**
	 * 执行建表语句（支持多条）
	 */
	public boolean createTable(String sql) {
		if (StringUtils.isBlank(sql)) {
			return false;
		}
		for (String statement : sql.split(";")) {
			if (StringUtils.isNotBlank(statement)) {
				jdbcTemplate.execute(statement);
			}
		}
		return true;
	}

	/**
	 * 根据id查询生成配置
	 */
	public GenTable getByTableId(Integer tableId) {
		return genTableMapper.selectById(tableId);
	}

	/**
	 * 根据表名查询生成配置
	 */
	public GenTable getByTableName(String tableName) {
		LambdaQueryWrapper<GenTable> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GenTable::getTableName, tableName);
		return genTableMapper.selectOne(wrapper);
	}

	/**
	 * 字段配置请求转实体
	 */
	private GenTableColumn toColumnEntity(SysGenTableColumnReq item) {
		GenTableColumn column = new GenTableColumn();
		column.setId(item.getId());
		column.setColumnComment(item.getColumnComment());
		column.setJavaField(item.getJavaField());
		column.setJavaType(item.getJavaType());
		column.setIsPk(item.getIsPk());
		column.setIsRequired(item.getIsRequired());
		column.setIsInsert(item.getIsInsert());
		column.setIsEdit(item.getIsEdit());
		column.setIsList(item.getIsList());
		column.setIsQuery(item.getIsQuery());
		column.setQueryType(item.getQueryType());
		column.setHtmlType(item.getHtmlType());
		column.setDictType(item.getDictType());
		column.setSort(item.getSort());
		return column;
	}

	/**
	 * 读取表字段元数据
	 */
	public List<GenTableColumn> readColumns(String tableName) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_DB_COLUMN, tableName);
		List<GenTableColumn> columns = new ArrayList<>();
		int sort = 1;
		for (Map<String, Object> row : rows) {
			String columnName = String.valueOf(row.get("columnName"));
			String columnType = String.valueOf(row.get("columnType"));
			String columnComment = row.get("columnComment") == null ? "" : String.valueOf(row.get("columnComment"));
			String columnKey = row.get("columnKey") == null ? "" : String.valueOf(row.get("columnKey"));
			String extra = row.get("extra") == null ? "" : String.valueOf(row.get("extra"));

			boolean isPk = COLUMN_KEY_PRIMARY.equalsIgnoreCase(columnKey);
			boolean autoIncrement = StringUtils.containsIgnoreCase(extra, EXTRA_AUTO_INCREMENT);
			String javaType = toJavaType(columnType);

			GenTableColumn column = new GenTableColumn();
			column.setColumnName(columnName);
			column.setColumnType(columnType);
			column.setColumnComment(columnComment);
			column.setJavaField(toJavaField(columnName));
			column.setJavaType(javaType);
			column.setIsPk(isPk ? "1" : "0");
			column.setIsRequired(isPk ? "1" : "0");
			column.setIsInsert(isPk && autoIncrement ? "0" : "1");
			column.setIsEdit(isPk && autoIncrement ? "0" : "1");
			column.setIsList(isPk ? "0" : "1");
			column.setIsQuery("0");
			column.setQueryType("EQ");
			column.setHtmlType(toHtmlType(javaType, columnName));
			column.setSort(sort++);
			columns.add(column);
		}
		return columns;
	}

	/**
	 * 读取表注释
	 */
	public String tableComment(String tableName) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_TABLE_COMMENT, tableName);
		if (rows.isEmpty()) {
			return "";
		}
		Object value = rows.get(0).get("table_comment");
		return value == null ? "" : String.valueOf(value);
	}

	/**
	 * 是否忽略的表
	 */
	private boolean isIgnore(String tableName) {
		return IGNORE_TABLES.stream().anyMatch(ignore -> ignore.equalsIgnoreCase(tableName));
	}

	/**
	 * 表名转类名（下划线转驼峰，首字母大写）
	 */
	public static String toClassName(String tableName) {
		String[] parts = tableName.toLowerCase().split("_");
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			if (part.isEmpty()) {
				continue;
			}
			sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		return sb.toString();
	}

	/**
	 * 模块名取表名前缀
	 */
	public static String toModuleName(String tableName) {
		int index = tableName.indexOf('_');
		return index > 0 ? tableName.substring(0, index) : tableName;
	}

	/**
	 * 字段名转属性名（下划线转驼峰，首字母小写）
	 */
	public static String toJavaField(String columnName) {
		String className = toClassName(columnName);
		if (className.isEmpty()) {
			return className;
		}
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}

	/**
	 * 数据库类型转 java 类型
	 */
	public static String toJavaType(String columnType) {
		String type = StringUtils.lowerCase(columnType);
		if (StringUtils.contains(type, "bigint")) {
			return "Long";
		}
		if (StringUtils.contains(type, "int")) {
			return "Integer";
		}
		if (StringUtils.containsAny(type, "decimal", "numeric")) {
			return "BigDecimal";
		}
		if (StringUtils.containsAny(type, "datetime", "timestamp")) {
			return "LocalDateTime";
		}
		if (StringUtils.contains(type, "date")) {
			return "LocalDate";
		}
		if (StringUtils.containsAny(type, "double", "float")) {
			return "Double";
		}
		return "String";
	}

	/**
	 * 推断表单展示类型
	 */
	private String toHtmlType(String javaType, String columnName) {
		if ("LocalDateTime".equals(javaType) || "LocalDate".equals(javaType)) {
			return "datetime";
		}
		if (StringUtils.containsIgnoreCase(columnName, "image")
				|| StringUtils.containsIgnoreCase(columnName, "avatar")) {
			return "imageUpload";
		}
		if (StringUtils.containsIgnoreCase(columnName, "file")) {
			return "fileUpload";
		}
		if (StringUtils.containsIgnoreCase(columnName, "content")) {
			return "editor";
		}
		return "input";
	}
}
