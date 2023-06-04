package com.company.codegenerator.content.java;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.company.codegenerator.db.DBUtil;
import com.company.codegenerator.util.Config;
import com.company.codegenerator.util.Constants;
import com.company.codegenerator.util.FileUtil.Replace;

import cn.hutool.core.io.FileUtil;

import com.company.codegenerator.util.NameUtil;

public class ControllerReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public ControllerReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		StringBuilder search_form = new StringBuilder();
		
		String notblank_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/notblank.java", Charset.forName("utf-8"));
		String notnull_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/notnull.java", Charset.forName("utf-8"));
		
		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			if(Constants.ID.equalsIgnoreCase(column_name)){
				continue;
			}
			
			String columnName = NameUtil.toCamelCase(column_name);
			String ColumnName = NameUtil.upperFirst(columnName);
			
			String dataType = map.get("DATA_TYPE");
			
			if (Constants.numberSet.contains(dataType)) {
				String notnull_tmp = notnull_tpl;
				notnull_tmp = notnull_tmp.replace("{column_name}", column_name);
				notnull_tmp = notnull_tmp.replace("{columnName}", columnName);
				notnull_tmp = notnull_tmp.replace("{ColumnName}", ColumnName);
				search_form.append(notnull_tmp);
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String notnull_tmp = notnull_tpl;
				notnull_tmp = notnull_tmp.replace("{column_name}", column_name);
				notnull_tmp = notnull_tmp.replace("{columnName}", columnName);
				notnull_tmp = notnull_tmp.replace("{ColumnName}", ColumnName);
				search_form.append(notnull_tmp);
			} else {
				// 文本框
				String notblank_tmp = notblank_tpl;
				notblank_tmp = notblank_tmp.replace("{column_name}", column_name);
				notblank_tmp = notblank_tmp.replace("{columnName}", columnName);
				notblank_tmp = notblank_tmp.replace("{ColumnName}", ColumnName);
				search_form.append(notblank_tmp);
			}
		}
		
		src = src.replace("{tab}", tab);
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		src = src.replace("{search_form}", search_form.toString());
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		
		return src;
	}
}