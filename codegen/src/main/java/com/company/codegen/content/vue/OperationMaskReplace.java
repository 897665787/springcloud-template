package com.company.codegen.content.vue;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;

import cn.hutool.core.io.FileUtil;

import com.company.codegen.util.NameUtil;

public class OperationMaskReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public OperationMaskReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {

		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		String modelNameCn = DBUtil.getCommentByTable(tab);

		StringBuilder add_form = new StringBuilder();
		StringBuilder input_required = new StringBuilder();
		StringBuilder form_ref = new StringBuilder();
		StringBuilder get_params = new StringBuilder();
		StringBuilder get_detail = new StringBuilder();
		StringBuilder init_dict = new StringBuilder();
		StringBuilder init_dict_func = new StringBuilder();

		List<String> get_detail_const = Lists.newArrayList();
		
		String form_input_text_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_input_text.vue", Charset.forName("utf-8"));
		String form_input_number_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_input_number.vue", Charset.forName("utf-8"));
		String form_input_radio_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_input_radio.vue", Charset.forName("utf-8"));
		String form_input_date_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_input_date.vue", Charset.forName("utf-8"));
		String form_input_select_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_input_select.vue", Charset.forName("utf-8"));

		String input_required_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/input_required.vue", Charset.forName("utf-8"));
		String form_ref_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/form_ref.vue", Charset.forName("utf-8"));
		String get_params_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/get_params.vue", Charset.forName("utf-8"));
		String get_detail_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/get_detail.vue", Charset.forName("utf-8"));
		String init_dict_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/init_dict.vue", Charset.forName("utf-8"));
		String init_dict_func_tpl = FileUtil.readString("classpath:code-generator-template/vue/operationmask/init_dict_func.vue", Charset.forName("utf-8"));

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
				String form_input_select_tmp = form_input_select_tpl;
				form_input_select_tmp = form_input_select_tmp.replace("{column_name}", column_name);
				form_input_select_tmp = form_input_select_tmp.replace("{columnName}", columnName);
				form_input_select_tmp = form_input_select_tmp.replace("{columnComment}", columnComment);
				add_form.append(form_input_select_tmp);

				String init_dict_tmp = init_dict_tpl;
				init_dict_tmp = init_dict_tmp.replace("{columnName}", columnName);
				init_dict_tmp = init_dict_tmp.replace("{ColumnName}", ColumnName);
				init_dict_tmp = init_dict_tmp.replace("{modelName}", modelName);
				init_dict.append(init_dict_tmp);
				String init_dict_func_tmp = init_dict_func_tpl;
				init_dict_func_tmp = init_dict_func_tmp.replace("{ColumnName}", ColumnName);
				init_dict_func.append(init_dict_func_tmp);
				
				String input_required_tmp = input_required_tpl;
				input_required_tmp = input_required_tmp.replace("{columnName}", columnName);
				input_required_tmp = input_required_tmp.replace("{columnComment}", columnComment);
				input_required.append(input_required_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String get_params_tmp = get_params_tpl;
				get_params_tmp = get_params_tmp.replace("{columnName}", columnName);
				get_params.append(get_params_tmp);
				
				String get_detail_tmp = get_detail_tpl;
				get_detail_tmp = get_detail_tmp.replace("{columnName}", columnName);
				get_detail.append(get_detail_tmp);
				
				get_detail_const.add(columnName);
			} else if (Constants.baseFieldSet.contains(column_name)) {
			} else if (Constants.dateSet.contains(dataType)) {
				// 日期框
				String form_input_date_tmp = form_input_date_tpl;
				form_input_date_tmp = form_input_date_tmp.replace("{column_name}", column_name);
				form_input_date_tmp = form_input_date_tmp.replace("{columnName}", columnName);
				form_input_date_tmp = form_input_date_tmp.replace("{columnComment}", columnComment);
				add_form.append(form_input_date_tmp);
				
				String input_required_tmp = input_required_tpl;
				input_required_tmp = input_required_tmp.replace("{columnName}", columnName);
				input_required_tmp = input_required_tmp.replace("{columnComment}", columnComment);
				input_required.append(input_required_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String get_params_tmp = get_params_tpl;
				get_params_tmp = get_params_tmp.replace("{columnName}", columnName);
				get_params.append(get_params_tmp);
				
				String get_detail_tmp = get_detail_tpl;
				get_detail_tmp = get_detail_tmp.replace("{columnName}", columnName);
				get_detail.append(get_detail_tmp);
				
				get_detail_const.add(columnName);
			} else if (Constants.numberSet.contains(dataType)) {
				// 数值框
				String form_input_number_tmp = form_input_number_tpl;
				form_input_number_tmp = form_input_number_tmp.replace("{column_name}", column_name);
				form_input_number_tmp = form_input_number_tmp.replace("{columnName}", columnName);
				form_input_number_tmp = form_input_number_tmp.replace("{columnComment}", columnComment);
				add_form.append(form_input_number_tmp);
				
				String input_required_tmp = input_required_tpl;
				input_required_tmp = input_required_tmp.replace("{columnName}", columnName);
				input_required_tmp = input_required_tmp.replace("{columnComment}", columnComment);
				input_required.append(input_required_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String get_params_tmp = get_params_tpl;
				get_params_tmp = get_params_tmp.replace("{columnName}", columnName);
				get_params.append(get_params_tmp);
				
				String get_detail_tmp = get_detail_tpl;
				get_detail_tmp = get_detail_tmp.replace("{columnName}", columnName);
				get_detail.append(get_detail_tmp);
				
				get_detail_const.add(columnName);
			} else {
				// 文本框
				String form_input_text_tmp = form_input_text_tpl;
				form_input_text_tmp = form_input_text_tmp.replace("{column_name}", column_name);
				form_input_text_tmp = form_input_text_tmp.replace("{columnName}", columnName);
				form_input_text_tmp = form_input_text_tmp.replace("{columnComment}", columnComment);
				add_form.append(form_input_text_tmp);
				
				String input_required_tmp = input_required_tpl;
				input_required_tmp = input_required_tmp.replace("{columnName}", columnName);
				input_required_tmp = input_required_tmp.replace("{columnComment}", columnComment);
				input_required.append(input_required_tmp);
				
				String form_ref_tmp = form_ref_tpl;
				form_ref_tmp = form_ref_tmp.replace("{columnName}", columnName);
				form_ref.append(form_ref_tmp);
				
				String get_params_tmp = get_params_tpl;
				get_params_tmp = get_params_tmp.replace("{columnName}", columnName);
				get_params.append(get_params_tmp);
				
				String get_detail_tmp = get_detail_tpl;
				get_detail_tmp = get_detail_tmp.replace("{columnName}", columnName);
				get_detail.append(get_detail_tmp);
				
				get_detail_const.add(columnName);
			}
		}
		
		src = src.replace("{module}", Config.get("module.name"));
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		src = src.replace("{modelNameCn}", modelNameCn);
		src = src.replace("{add_form}", add_form.toString());
		src = src.replace("{input_required}", input_required.toString());
		src = src.replace("{form_ref}", form_ref.toString());
		src = src.replace("{get_params}", get_params.toString());
		src = src.replace("{get_detail}", get_detail.toString());
		src = src.replace("{get_detail_const}", get_detail_const.stream().collect(Collectors.joining(", ")));
		src = src.replace("{init_dict}", init_dict.toString());
		src = src.replace("{init_dict_func}", init_dict_func.toString());
		return src;
	}
}