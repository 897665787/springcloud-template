package com.company.codegenerator.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.codegenerator.util.Config;

public class DBUtil {

	/**
	 * 根据表名查询列属性
	 * @param tab 表名
	 * @return
	 */
	public static List<Map<String, String>> getColumnsByTable(String tab) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection conn = DB.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT,EXTRA,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns where TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
			pst = conn.prepareStatement(sql);
			
			String url = Config.get("url");
			String schema = url.substring(url.lastIndexOf('/') + 1);
			pst.setString(1, schema);
			pst.setString(2, tab);
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
				map.put("DATA_TYPE", rs.getString("DATA_TYPE"));
				map.put("COLUMN_KEY", rs.getString("COLUMN_KEY"));
				map.put("COLUMN_COMMENT", rs.getString("COLUMN_COMMENT"));
				map.put("EXTRA", rs.getString("EXTRA"));
				map.put("IS_NULLABLE", rs.getString("IS_NULLABLE"));
				map.put("CHARACTER_MAXIMUM_LENGTH", rs.getString("CHARACTER_MAXIMUM_LENGTH"));
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pst, rs);
		}
		return list;
	}
	
	/**
	 * 根据表名查询表备注
	 * @param tab 表名
	 * @return
	 */
	public static String getCommentByTable(String tab) {
		Connection conn = DB.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT TABLE_COMMENT FROM information_schema.tables where TABLE_SCHEMA = ? AND TABLE_NAME = ?";
			pst = conn.prepareStatement(sql);
			
			String url = Config.get("url");
			String schema = url.substring(url.lastIndexOf('/') + 1);
			pst.setString(1, schema);
			pst.setString(2, tab);
			rs = pst.executeQuery();
			while (rs.next()) {
				String comment = rs.getString("TABLE_COMMENT");
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
			DB.close(conn, pst, rs);
		}
		return "";
	}
}
