package com.company.codegenerator.content.html;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.company.codegenerator.db.DBUtil;
import com.company.codegenerator.util.Config;
import com.company.codegenerator.util.Constants;
import com.company.codegenerator.util.FileUtil.Replace;
import com.company.codegenerator.util.NameUtil;

import cn.hutool.core.io.FileUtil;

public class IndexReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public IndexReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		
		String search_form_text_tpl = FileUtil.readString("classpath:code-generator-template/jsp/search/search_form_text.jsp", Charset.forName("utf-8"));
		String search_form_timerange_tpl = FileUtil.readString("classpath:code-generator-template/jsp/search/search_form_timerange.jsp", Charset.forName("utf-8"));
		String search_form_select_tpl = FileUtil.readString("classpath:code-generator-template/jsp/search/search_form_select.jsp", Charset.forName("utf-8"));
		
		String table_th_tpl = FileUtil.readString("classpath:code-generator-template/jsp/tablelist/table_th.jsp", Charset.forName("utf-8"));
		String table_td_text_tpl = FileUtil.readString("classpath:code-generator-template/jsp/tablelist/table_td_text.jsp", Charset.forName("utf-8"));
		String table_td_select_tpl = FileUtil.readString("classpath:code-generator-template/jsp/tablelist/table_td_select.jsp", Charset.forName("utf-8"));
		String table_td_date_tpl = FileUtil.readString("classpath:code-generator-template/jsp/tablelist/table_td_date.jsp", Charset.forName("utf-8"));
		String table_td_image_tpl = FileUtil.readString("classpath:code-generator-template/jsp/tablelist/table_td_image.jsp", Charset.forName("utf-8"));
		
		StringBuilder search_form = new StringBuilder();
		StringBuilder table_th = new StringBuilder();
		StringBuilder table_td = new StringBuilder();
		
		int table_th_count = 0;
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
			
			String dataType = map.get("DATA_TYPE");
			
			boolean isImageUrl = column_name.contains("image") || column_name.contains("url") || column_name.contains("logo");
			
			List<String[]> selectList = NameUtil.selectItem(columnComment);
			
			if (!selectList.isEmpty()) {
				String search_form_select_tmp = search_form_select_tpl;
				search_form_select_tmp = search_form_select_tmp.replace("{columnName}", columnName);
				search_form_select_tmp = search_form_select_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form_select_tmp = search_form_select_tmp.replace("{module}", Config.get("module.name"));
				search_form.append(search_form_select_tmp);
				
				table_td.append(table_td_select_tpl.replace("{columnName}", columnName));
				table_th.append(table_th_tpl.replace("{simpleColumnComment}", simpleColumnComment));
				table_th_count++;
			} else if (isImageUrl) {
				table_td.append(table_td_image_tpl.replace("{columnName}", columnName));
				table_th.append(table_th_tpl.replace("{simpleColumnComment}", simpleColumnComment));
				table_th_count++;
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String search_form_timerange_tmp = search_form_timerange_tpl;
				search_form_timerange_tmp = search_form_timerange_tmp.replace("{columnName}", columnName);
				search_form_timerange_tmp = search_form_timerange_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form.append(search_form_timerange_tmp);
				
				table_td.append(table_td_date_tpl.replace("{columnName}", columnName));
				table_th.append(table_th_tpl.replace("{simpleColumnComment}", simpleColumnComment));
				table_th_count++;
			} else {
				// 文本框
				String search_form_text_tmp = search_form_text_tpl;
				search_form_text_tmp = search_form_text_tmp.replace("{columnName}", columnName);
				search_form_text_tmp = search_form_text_tmp.replace("{simpleColumnComment}", simpleColumnComment);
				search_form.append(search_form_text_tmp);
				
				table_td.append(table_td_text_tpl.replace("{columnName}", columnName));
				table_th.append(table_th_tpl.replace("{simpleColumnComment}", simpleColumnComment));
				table_th_count++;
			}
		}
		
		src = src.replace("{search_form}", search_form.toString());
		src = src.replace("{table_th}", table_th.toString());
		src = src.replace("{table_th_count}", String.valueOf(table_th_count + 1));
		src = src.replace("{table_td}", table_td.toString());
		
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(Config.get("table_prefix"), ""));
		String ModelName = NameUtil.upperFirst(modelName);

		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{key_property}", Constants.ID);
		
		return src;
	}
}