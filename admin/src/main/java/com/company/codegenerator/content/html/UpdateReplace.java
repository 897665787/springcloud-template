package com.company.codegenerator.content.html;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.company.codegenerator.db.DBUtil;
import com.company.codegenerator.util.Config;
import com.company.codegenerator.util.Constants;
import com.company.codegenerator.util.FileUtil.Replace;

import cn.hutool.core.io.FileUtil;

import com.company.codegenerator.util.NameUtil;

public class UpdateReplace implements Replace {
	List<Map<String, String>> datas = null;
	String tab;
	
	public UpdateReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		String input_form_num_tpl = FileUtil.readString("classpath:code-generator-template/jsp/update/input_form_num.jsp", Charset.forName("utf-8"));
		String input_form_select_tpl = FileUtil.readString("classpath:code-generator-template/jsp/update/input_form_select.jsp", Charset.forName("utf-8"));
		String input_form_text_tpl = FileUtil.readString("classpath:code-generator-template/jsp/update/input_form_text.jsp", Charset.forName("utf-8"));
		String input_form_image_tpl = FileUtil.readString("classpath:code-generator-template/jsp/update/input_form_image.jsp", Charset.forName("utf-8"));
		
		String rule_num_tpl = FileUtil.readString("classpath:code-generator-template/jsp/indexSingle/rule_num.jsp", Charset.forName("utf-8"));
		String rule_text_tpl = FileUtil.readString("classpath:code-generator-template/jsp/indexSingle/rule_text.jsp", Charset.forName("utf-8"));
		
		String rule_message_num_tpl = FileUtil.readString("classpath:code-generator-template/jsp/indexSingle/rule_message_num.jsp", Charset.forName("utf-8"));
		String rule_message_text_tpl = FileUtil.readString("classpath:code-generator-template/jsp/indexSingle/rule_message_text.jsp", Charset.forName("utf-8"));
		
		StringBuilder save_form = new StringBuilder();
		StringBuilder create_validate_rules = new StringBuilder();
		StringBuilder create_validate_messages = new StringBuilder();
		
		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			if(Constants.ID.equalsIgnoreCase(column_name)){
				continue;
			}
			
			if (Constants.ignoreFieldSet.contains(column_name)) {
				continue;
			}
			
			String columnComment = map.get("COLUMN_COMMENT");
			String simpleColumnComment = NameUtil.columnName(columnComment);
			
			String columnName = NameUtil.toCamelCase(column_name);
			String ColumnName = NameUtil.upperFirst(columnName);
			
			String dataType = map.get("DATA_TYPE");
			String character_maximum_length = map.get("CHARACTER_MAXIMUM_LENGTH");
			
			List<String[]> selectList = NameUtil.selectItem(columnComment);
			
			String isNullable = map.get("IS_NULLABLE");
			String required = "NO".equalsIgnoreCase(isNullable) ? " required":"";
			
			boolean isImageUrl = column_name.contains("image") || column_name.contains("url") || column_name.contains("logo");
			
			if (!selectList.isEmpty()) {
				String input_form_select_tmp = input_form_select_tpl
						.replace("{required}", required)
						.replace("{columnName}", columnName)
						.replace("{ColumnName}", ColumnName)
						.replace("{simpleColumnComment}", simpleColumnComment)
						.replace("{module}", Config.get("module.name")
								);
						save_form.append(input_form_select_tmp);
				
			} else if (isImageUrl) {
				String input_form_image_tmp = input_form_image_tpl
						.replace("{required}", required)
						.replace("{columnName}", columnName)
						.replace("{ColumnName}", ColumnName)
						.replace("{simpleColumnComment}", simpleColumnComment);
				save_form.append(input_form_image_tmp);
				
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String input_form_text_tmp = input_form_text_tpl
						.replace("{required}", required)
						.replace("{columnName}", columnName)
						.replace("{ColumnName}", ColumnName)
						.replace("{simpleColumnComment}", simpleColumnComment);
						save_form.append(input_form_text_tmp);
						
			} else if (Constants.numberSet.contains(dataType)) {
				// 数字框
				String input_form_num_tmp = input_form_num_tpl
						.replace("{required}", required)
						.replace("{columnName}", columnName)
						.replace("{ColumnName}", ColumnName)
						.replace("{simpleColumnComment}", simpleColumnComment);
						save_form.append(input_form_num_tmp);
			} else {
				// 文本框
				String input_form_text_tmp = input_form_text_tpl
						.replace("{required}", required)
						.replace("{columnName}", columnName)
						.replace("{ColumnName}", ColumnName)
						.replace("{simpleColumnComment}", simpleColumnComment);
						save_form.append(input_form_text_tmp);
			}
			
			if("NO".equalsIgnoreCase(isNullable)){
				if(!selectList.isEmpty() || Constants.numberSet.contains(dataType) || Constants.dateSet.contains(dataType)){

					create_validate_rules.append(rule_num_tpl.replace("{columnName}", columnName));
					
					create_validate_messages.append(
							rule_message_num_tpl.replace("{columnName}", columnName)
							.replace("{simpleColumnComment}", simpleColumnComment));
					
				} else {
					create_validate_rules.append(rule_text_tpl.replace("{columnName}", columnName)
							.replace("{character_maximum_length}", character_maximum_length));
					
					create_validate_messages.append(rule_message_text_tpl.replace("{columnName}", columnName)
							.replace("{character_maximum_length}", character_maximum_length)
							.replace("{simpleColumnComment}", simpleColumnComment));
				}
			}
		}
		
		src = src.replace("{save_form}", save_form.toString());
		src = src.replace("{create_validate_rules}", create_validate_rules.toString());
		src = src.replace("{create_validate_messages}", create_validate_messages.toString());
		
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{key_property}", Constants.ID);
		
		return src;
	}
}