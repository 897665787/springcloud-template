package com.company.codegen.content.java;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;

import cn.hutool.core.io.FileUtil;

import com.company.codegen.util.NameUtil;

public class ControllerReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public ControllerReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		StringBuilder column_field_feign = new StringBuilder();
		StringBuilder column_field = new StringBuilder();
		
		StringBuilder search_form = new StringBuilder();
		
		String query_wrapper_simple_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/query_wrapper_simple.java", Charset.forName("utf-8"));
		String query_wrapper_text_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/query_wrapper_text.java", Charset.forName("utf-8"));
		String query_wrapper_text_option_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/query_wrapper_text_option.java", Charset.forName("utf-8"));
		String query_wrapper_date_tpl = FileUtil.readString("classpath:code-generator-template/java/controller/query_wrapper_date.java", Charset.forName("utf-8"));
		String field_feign_tpl = ", {dataType} {columnName}";
		
		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			
			String columnComment = map.get("COLUMN_COMMENT");
			
			String columnName = NameUtil.toCamelCase(column_name);
			
			String dataType = map.get("DATA_TYPE");
			
			List<String[]> selectList = NameUtil.selectItem(columnComment);
			
			if (Constants.ID.equalsIgnoreCase(column_name)) {
			} else if (!selectList.isEmpty()) {
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName);
				field_feign_tmp = field_feign_tmp.replace("{dataType}", Config.get(dataType));
				column_field_feign.append(field_feign_tmp);
				column_field.append(", " + columnName);
				if (Constants.numberSet.contains(dataType) || Constants.dateSet.contains(dataType)) {
					String query_wrapper_simple_tmp = query_wrapper_simple_tpl;
					query_wrapper_simple_tmp = query_wrapper_simple_tmp.replace("{column_name}", column_name);
					query_wrapper_simple_tmp = query_wrapper_simple_tmp.replace("{columnName}", columnName);
					search_form.append(query_wrapper_simple_tmp);
				} else {
					String query_wrapper_text_option_tmp = query_wrapper_text_option_tpl;
					query_wrapper_text_option_tmp = query_wrapper_text_option_tmp.replace("{column_name}", column_name);
					query_wrapper_text_option_tmp = query_wrapper_text_option_tmp.replace("{columnName}", columnName);
					search_form.append(query_wrapper_text_option_tmp);
				}
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName + "Start");
				field_feign_tmp = field_feign_tmp.replace("{dataType}", "String");
				column_field_feign.append(field_feign_tmp);
				column_field.append(", " + columnName + "Start");
				field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName + "End");
				field_feign_tmp = field_feign_tmp.replace("{dataType}", "String");
				column_field_feign.append(field_feign_tmp);
				column_field.append(", " + columnName + "End");
				
				String query_wrapper_date_tmp = query_wrapper_date_tpl;
				query_wrapper_date_tmp = query_wrapper_date_tmp.replace("{column_name}", column_name);
				query_wrapper_date_tmp = query_wrapper_date_tmp.replace("{columnName}", columnName);
				search_form.append(query_wrapper_date_tmp);
			} else if (Constants.baseFieldSet.contains(column_name)) {
			} else if (Constants.numberSet.contains(dataType)) {
				// 文本框
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName);
				field_feign_tmp = field_feign_tmp.replace("{dataType}", Config.get(dataType));
				column_field_feign.append(field_feign_tmp);
				column_field.append(", " + columnName);
				
				String query_wrapper_simple_tmp = query_wrapper_simple_tpl;
				query_wrapper_simple_tmp = query_wrapper_simple_tmp.replace("{column_name}", column_name);
				query_wrapper_simple_tmp = query_wrapper_simple_tmp.replace("{columnName}", columnName);
				search_form.append(query_wrapper_simple_tmp);
			} else {
				// 文本框
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName);
				field_feign_tmp = field_feign_tmp.replace("{dataType}", Config.get(dataType));
				column_field_feign.append(field_feign_tmp);
				column_field.append(", " + columnName);
				
				String query_wrapper_text_tmp = query_wrapper_text_tpl;
				query_wrapper_text_tmp = query_wrapper_text_tmp.replace("{column_name}", column_name);
				query_wrapper_text_tmp = query_wrapper_text_tmp.replace("{columnName}", columnName);
				search_form.append(query_wrapper_text_tmp);
			}
		}
		
		src = src.replace("{module}", Config.get("module.name"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{page_column_field_feign}", "Long current, Long size" + column_field_feign.toString());
		src = src.replace("{column_field_feign}", column_field_feign.toString().replaceFirst(", ", ""));
		src = src.replace("{column_field}", column_field.toString().replaceFirst(", ", ""));
		src = src.replace("{search_form}", search_form.toString());
		
		return src;
	}
}