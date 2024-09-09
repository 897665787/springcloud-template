package com.company.codegen.content.java;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;
import com.google.common.collect.Lists;

import cn.hutool.core.io.FileUtil;

public class ModelReqReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public ModelReqReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
	
		StringBuilder column_field = new StringBuilder();
		StringBuilder save_form = new StringBuilder();
		
		String field_id_tpl = FileUtil.readString("classpath:code-generator-template/java/model/field_id.java", Charset.forName("utf-8"));
		String field_simple_tpl = FileUtil.readString("classpath:code-generator-template/java/model/field_simple.java", Charset.forName("utf-8"));
		String field_simple_select_tpl = FileUtil.readString("classpath:code-generator-template/java/model/field_simple_select.java", Charset.forName("utf-8"));
		
		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			
			String columnComment = map.get("COLUMN_COMMENT");
			
			String columnName = NameUtil.toCamelCase(column_name);
			
			String dataType = map.get("DATA_TYPE");
			
			List<String[]> selectList = NameUtil.selectItem(columnComment);
			
			if (Constants.ID.equalsIgnoreCase(column_name)) {
				String field_id_tmp = field_id_tpl;
				field_id_tmp = field_id_tmp.replace("{columnName}", columnName);
				field_id_tmp = field_id_tmp.replace("{column_name}", column_name);
				field_id_tmp = field_id_tmp.replace("{columnComment}", columnComment);
				field_id_tmp = field_id_tmp.replace("{dataType}", Config.get(dataType));
				column_field.append(field_id_tmp);
			} else if (!selectList.isEmpty()) {
				List<String> list = Lists.newArrayList();
				for (String[] kv : selectList) {
					String k = kv[0];
					String v = kv[1];
					list.add(String.format("\"%s:%s\"", k,v));
				}
				String autoDescStr = list.stream().collect(Collectors.joining(","));
				
				String field_simple_select_tmp = field_simple_select_tpl;
				field_simple_select_tmp = field_simple_select_tmp.replace("{autoDescStr}", autoDescStr);
				field_simple_select_tmp = field_simple_select_tmp.replace("{columnName}", columnName);
				field_simple_select_tmp = field_simple_select_tmp.replace("{column_name}", column_name);
				field_simple_select_tmp = field_simple_select_tmp.replace("{columnComment}", columnComment);
				field_simple_select_tmp = field_simple_select_tmp.replace("{dataType}", Config.get(dataType));
				column_field.append(field_simple_select_tmp);
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String field_simple_tmp = field_simple_tpl;
				field_simple_tmp = field_simple_tmp.replace("{columnName}", columnName);
				field_simple_tmp = field_simple_tmp.replace("{column_name}", column_name);
				field_simple_tmp = field_simple_tmp.replace("{columnComment}", columnComment);
				field_simple_tmp = field_simple_tmp.replace("{dataType}", Config.get(dataType));
				column_field.append(field_simple_tmp);
			} else {
				// 文本框
				String field_simple_tmp = field_simple_tpl;
				field_simple_tmp = field_simple_tmp.replace("{columnName}", columnName);
				field_simple_tmp = field_simple_tmp.replace("{column_name}", column_name);
				field_simple_tmp = field_simple_tmp.replace("{columnComment}", columnComment);
				field_simple_tmp = field_simple_tmp.replace("{dataType}", Config.get(dataType));
				column_field.append(field_simple_tmp);
			}
		}
		
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		src = src.replace("{table}", tab);
		
		String ModelName = NameUtil.dealClassName(tab);
		String modelName = ModelName.substring(0, 1).toLowerCase()+ModelName.substring(1);
		src = src.replace("{column_field}", column_field.toString());
		src = src.replace("{save_form}", save_form.toString());
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{key_property}", Constants.ID);
		
		return src;
	}
}