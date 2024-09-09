package com.company.codegen.content.sql;

import cn.hutool.core.io.FileUtil;
import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class DictReplace implements Replace {

	List<Map<String, String>> datas = null;
	String tab;

	public DictReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		String modelNameCn = DBUtil.getCommentByTable(tab);

		StringBuilder dict_type = new StringBuilder();
		StringBuilder dict_data = new StringBuilder();

		String dict_type_tpl = FileUtil.readString("classpath:code-generator-template/sql/dict/dict_type.sql", Charset.forName("utf-8"));
		String dict_data_tpl = FileUtil.readString("classpath:code-generator-template/sql/dict/dict_data.sql", Charset.forName("utf-8"));

        for (Map<String, String> map : datas) {
			String column_name = map.get("COLUMN_NAME");
			String columnComment = map.get("COLUMN_COMMENT");
			String columnName = NameUtil.toCamelCase(column_name);
			String columnNameCn = columnComment;
			int idx = columnNameCn.indexOf("(");
			if (idx != -1) {
				columnNameCn = columnNameCn.substring(0, idx);
			}
			String dataType = map.get("DATA_TYPE");

			List<String[]> selectList = NameUtil.selectItem(columnComment);

			if (selectList.isEmpty()) {
				continue;
			}

			String dict_type_tmp = dict_type_tpl;
			dict_type_tmp = dict_type_tmp.replace("{modelNameCn}", modelNameCn);
			dict_type_tmp = dict_type_tmp.replace("{columnNameCn}", columnNameCn);
			dict_type_tmp = dict_type_tmp.replace("{modelName}", modelName);
			dict_type_tmp = dict_type_tmp.replace("{columnName}", columnName);
			dict_type.append(dict_type_tmp);

            for (int i = 0; i < selectList.size(); i++) {
                String[] arr = selectList.get(i);
                String code = arr[0], value = arr[1];
                String dict_data_tmp = dict_data_tpl;
                dict_data_tmp = dict_data_tmp.replace("{modelName}", modelName);
                dict_data_tmp = dict_data_tmp.replace("{columnName}", columnName);
                dict_data_tmp = dict_data_tmp.replace("{dictCode}", code);
                dict_data_tmp = dict_data_tmp.replace("{dictValue}", value);
                dict_data_tmp = dict_data_tmp.replace("{index}", String.valueOf(i));
                dict_data.append(dict_data_tmp);
            }
        }

        src = src.replace("{dict_type}", dict_type.toString());
		src = src.replace("{dict_data}", dict_data.toString());

		return src;
	}
}