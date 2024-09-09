package com.company.codegen.content.java;

import java.util.List;
import java.util.Map;

import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;

public class FeignReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public FeignReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		StringBuilder column_field_feign = new StringBuilder();
		String field_feign_tpl = ", @RequestParam(value = \"{columnName}\", required = false) {dataType} {columnName}";		
		
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
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName + "Start");
				field_feign_tmp = field_feign_tmp.replace("{dataType}", "String");
				column_field_feign.append(field_feign_tmp);
				field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName + "End");
				field_feign_tmp = field_feign_tmp.replace("{dataType}", "String");
				column_field_feign.append(field_feign_tmp);
			} else if (Constants.baseFieldSet.contains(column_name)) {
			} else {
				// 文本框
				String field_feign_tmp = field_feign_tpl;
				field_feign_tmp = field_feign_tmp.replace("{columnName}", columnName);
				field_feign_tmp = field_feign_tmp.replace("{dataType}", Config.get(dataType));
				column_field_feign.append(field_feign_tmp);
			}
		}
		
		src = src.replace("{module}", Config.get("module.name"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{page_column_field_feign}", "@RequestParam(value = \"current\") Long current, @RequestParam(value = \"size\") Long size" + column_field_feign.toString());
		src = src.replace("{column_field_feign}", column_field_feign.toString().replaceFirst(", ", ""));
		
		return src;
	}
}