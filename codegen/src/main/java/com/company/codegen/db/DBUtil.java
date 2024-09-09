package com.company.codegen.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.codegen.util.Config;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

public class DBUtil {

	/**
	 * 根据表名查询列属性
	 * @param tab 表名
	 * @return
	 */
	public static List<Map<String, String>> getColumnsByTable(String tab) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String sql = "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT,EXTRA,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns where TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
			
			String schema = Config.get("schema");
			
			List<Entity> entityList = Db.use().query(sql, schema, tab);
			for (Entity entity : entityList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("COLUMN_NAME", entity.getStr("COLUMN_NAME"));
				map.put("DATA_TYPE", entity.getStr("DATA_TYPE"));
				map.put("COLUMN_KEY", entity.getStr("COLUMN_KEY"));
				map.put("COLUMN_COMMENT", entity.getStr("COLUMN_COMMENT"));
				map.put("EXTRA", entity.getStr("EXTRA"));
				map.put("IS_NULLABLE", entity.getStr("IS_NULLABLE"));
				map.put("CHARACTER_MAXIMUM_LENGTH", entity.getStr("CHARACTER_MAXIMUM_LENGTH"));
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return list;
	}
	
	/**
	 * 根据表名查询表备注
	 * @param tab 表名
	 * @return
	 */
	public static String getCommentByTable(String tab) {
		try {
			String sql = "SELECT TABLE_COMMENT FROM information_schema.tables where TABLE_SCHEMA = ? AND TABLE_NAME = ?";
			
			String schema = Config.get("schema");
			
			List<Entity> entityList = Db.use().query(sql, schema, tab);
			for (Entity entity : entityList) {
				String comment = entity.getStr("TABLE_COMMENT");
				if (comment == null) {
					continue;
				}
				// 如果最后是表字则去掉表字
				String biao = comment.substring(comment.length() - 1);
				if (biao.equals("表")) {
					return comment.substring(0, comment.length() - 1);
				}
				return comment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return "";
	}
}
