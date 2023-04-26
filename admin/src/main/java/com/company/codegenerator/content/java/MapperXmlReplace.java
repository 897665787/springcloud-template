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

public class MapperXmlReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public MapperXmlReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		StringBuilder search_form = new StringBuilder();
		StringBuilder table_th = new StringBuilder();
		StringBuilder table_td = new StringBuilder();
		
		String condition_num_tpl = FileUtil.readString("classpath:code-generator-template/java/mapperxml/condition_num.xml", Charset.forName("utf-8"));
		String condition_text_tpl = FileUtil.readString("classpath:code-generator-template/java/mapperxml/condition_text.xml", Charset.forName("utf-8"));
		String condition_date_tpl = FileUtil.readString("classpath:code-generator-template/java/mapperxml/condition_date.xml", Charset.forName("utf-8"));
		
		int table_th_count = 0;
		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			if(Constants.ID.equalsIgnoreCase(column_name)){
				continue;
			}
			
			String columnComment = map.get("COLUMN_COMMENT");
			String simpleColumnComment = NameUtil.columnName(columnComment);
			
			String columnName = NameUtil.toCamelCase(column_name);
			
			String dataType = map.get("DATA_TYPE");
			
			if (Constants.numberSet.contains(dataType)) {
				String condition_num_tmp = condition_num_tpl;
				condition_num_tmp = condition_num_tmp.replace("{column_name}", column_name);
				condition_num_tmp = condition_num_tmp.replace("{columnName}", columnName);
				condition_num_tmp = condition_num_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form.append(condition_num_tmp);
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String condition_date_tmp = condition_date_tpl;
				condition_date_tmp = condition_date_tmp.replace("{column_name}", column_name);
				condition_date_tmp = condition_date_tmp.replace("{columnName}", columnName);
				condition_date_tmp = condition_date_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form.append(condition_date_tmp);
			} else {
				// 文本框
				String condition_text_tmp = condition_text_tpl;
				condition_text_tmp = condition_text_tmp.replace("{column_name}", column_name);
				condition_text_tmp = condition_text_tmp.replace("{columnName}", columnName);
				condition_text_tmp = condition_text_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form.append(condition_text_tmp);
			}
		}
		
		src = src.replace("{tab}", tab);
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		src = src.replace("{search_form}", search_form.toString());
		src = src.replace("{table_th}", table_th.toString());
		src = src.replace("{table_th_count}", String.valueOf(table_th_count + 1));
		src = src.replace("{table_td}", table_td.toString());

		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{key_property}", Constants.ID);
		
		return src;
	}
}