package com.company.codegen.content.vue;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;

import cn.hutool.core.io.FileUtil;

public class IndexvueReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public IndexvueReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);

		StringBuilder search_form = new StringBuilder();
		StringBuilder form_ref = new StringBuilder();
		StringBuilder table_col = new StringBuilder();
		StringBuilder request_for_list = new StringBuilder();
		StringBuilder init_dict = new StringBuilder();
		StringBuilder init_dict_func = new StringBuilder();
		StringBuilder table_slot = new StringBuilder();
		
		String query_form_input_text_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/query_form_input_text.vue", Charset.forName("utf-8"));
		String query_form_input_number_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/query_form_input_number.vue", Charset.forName("utf-8"));
		String query_form_input_select_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/query_form_input_select.vue", Charset.forName("utf-8"));
		String query_form_input_daterange_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/query_form_input_daterange.vue", Charset.forName("utf-8"));
		
		String form_ref_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/form_ref.vue", Charset.forName("utf-8"));
		String form_ref_daterange_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/form_ref_daterange.vue", Charset.forName("utf-8"));
		String table_col_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/table_col.vue", Charset.forName("utf-8"));
		String request_for_list_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/request_for_list.vue", Charset.forName("utf-8"));
		String request_for_list_daterange_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/request_for_list_daterange.vue", Charset.forName("utf-8"));
		String init_dict_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/init_dict.vue", Charset.forName("utf8"));
		String init_dict_func_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/init_dict_func.vue", Charset.forName("utf8"));

		String table_slot_dict_tpl = FileUtil.readString("classpath:code-generator-template/vue/indexvue/table_slot_dict.vue", Charset.forName("utf8"));

		for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			
			String columnComment = map.get("COLUMN_COMMENT");
			
			String columnName = NameUtil.toCamelCase(column_name);
			String ColumnName = NameUtil.upperFirst(columnName);
			
			String dataType = map.get("DATA_TYPE");
			
			List<String[]> selectList = NameUtil.selectItem(columnComment);
			columnComment = NameUtil.columnComment(columnComment);
			
			if (Constants.ID.equalsIgnoreCase(column_name)) {
				
			} else if (!selectList.isEmpty()) {
				String query_form_input_select_tmp = query_form_input_select_tpl;
				query_form_input_select_tmp = query_form_input_select_tmp.replace("{column_name}", column_name);
				query_form_input_select_tmp = query_form_input_select_tmp.replace("{columnName}", columnName);
				query_form_input_select_tmp = query_form_input_select_tmp.replace("{columnComment}", columnComment);
				search_form.append(query_form_input_select_tmp);

				String init_dict_tmp = init_dict_tpl;
				init_dict_tmp = init_dict_tmp.replace("{columnName}", columnName);
				init_dict_tmp = init_dict_tmp.replace("{ColumnName}", ColumnName);
				init_dict_tmp = init_dict_tmp.replace("{modelName}", modelName);
				init_dict.append(init_dict_tmp);
				String init_dict_func_tmp = init_dict_func_tpl;
				init_dict_func_tmp = init_dict_func_tmp.replace("{ColumnName}", ColumnName);
				init_dict_func.append(init_dict_func_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);

				String table_col_tmp = table_col_tpl;
				table_col_tmp = table_col_tmp.replace("{columnName}", columnName);
				table_col_tmp = table_col_tmp.replace("{columnComment}", columnComment);
				table_col.append(table_col_tmp);

				String table_slot_dict_tmp = table_slot_dict_tpl;
				table_slot_dict_tmp = table_slot_dict_tmp.replace("{columnName}", columnName);
				table_slot.append(table_slot_dict_tmp);
				
				String request_for_list_tmp = request_for_list_tpl;
				request_for_list_tmp = request_for_list_tmp.replace("{columnName}", columnName);
				request_for_list.append(request_for_list_tmp);
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String query_form_input_daterange_tmp = query_form_input_daterange_tpl;
				query_form_input_daterange_tmp = query_form_input_daterange_tmp.replace("{column_name}", column_name);
				query_form_input_daterange_tmp = query_form_input_daterange_tmp.replace("{columnName}", columnName);
				query_form_input_daterange_tmp = query_form_input_daterange_tmp.replace("{columnComment}", columnComment);
				search_form.append(query_form_input_daterange_tmp);
				
				String form_ref_tmp = form_ref_daterange_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String table_col_tmp = table_col_tpl;
				table_col_tmp = table_col_tmp.replace("{columnName}", columnName);
				table_col_tmp = table_col_tmp.replace("{columnComment}", columnComment);
				table_col.append(table_col_tmp);
				
				String request_for_list_daterange_tmp = request_for_list_daterange_tpl;
				request_for_list_daterange_tmp = request_for_list_daterange_tmp.replace("{columnName}", columnName);
				request_for_list.append(request_for_list_daterange_tmp);
			} else if (Constants.baseFieldSet.contains(column_name)) {
			} else if (Constants.numberSet.contains(dataType)) {
				// 数值框
				String query_form_input_number_tmp = query_form_input_number_tpl;
				query_form_input_number_tmp = query_form_input_number_tmp.replace("{column_name}", column_name);
				query_form_input_number_tmp = query_form_input_number_tmp.replace("{columnName}", columnName);
				query_form_input_number_tmp = query_form_input_number_tmp.replace("{columnComment}", columnComment);
				search_form.append(query_form_input_number_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String table_col_tmp = table_col_tpl;
				table_col_tmp = table_col_tmp.replace("{columnName}", columnName);
				table_col_tmp = table_col_tmp.replace("{columnComment}", columnComment);
				table_col.append(table_col_tmp);
				
				String request_for_list_tmp = request_for_list_tpl;
				request_for_list_tmp = request_for_list_tmp.replace("{columnName}", columnName);
				request_for_list.append(request_for_list_tmp);
			} else {
				// 文本框
				String query_form_input_text_tmp = query_form_input_text_tpl;
				query_form_input_text_tmp = query_form_input_text_tmp.replace("{column_name}", column_name);
				query_form_input_text_tmp = query_form_input_text_tmp.replace("{columnName}", columnName);
				query_form_input_text_tmp = query_form_input_text_tmp.replace("{columnComment}", columnComment);
				search_form.append(query_form_input_text_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String table_col_tmp = table_col_tpl;
				table_col_tmp = table_col_tmp.replace("{columnName}", columnName);
				table_col_tmp = table_col_tmp.replace("{columnComment}", columnComment);
				table_col.append(table_col_tmp);
				
				String request_for_list_tmp = request_for_list_tpl;
				request_for_list_tmp = request_for_list_tmp.replace("{columnName}", columnName);
				request_for_list.append(request_for_list_tmp);
			}
		}
		
		src = src.replace("{module}", Config.get("module.name"));
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{search_form}", search_form.toString());
		src = src.replace("{form_ref}", form_ref.toString());
		src = src.replace("{table_col}", table_col.toString());
		src = src.replace("{request_for_list}", request_for_list.toString());
		src = src.replace("{init_dict}", init_dict.toString());
		src = src.replace("{init_dict_func}", init_dict_func.toString());
		src = src.replace("{table_slot}", table_slot.toString());
		
		return src;
	}
}