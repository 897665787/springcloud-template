package com.company.codegen.content.java;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.Constants;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelExcelReplace implements Replace {

    List<Map<String, String>> datas = null;
    String tab;

    public ModelExcelReplace(String tab) {
        datas = DBUtil.getColumnsByTable(tab);
        this.tab = tab;
    }

    @Override
    public String doReplace(String src) {

        StringBuilder column_field = new StringBuilder();
        StringBuilder save_form = new StringBuilder();

        final String field_excel_tpl = FileUtil.readString("classpath:code-generator-template/java/model/field_excel.java", Charset.forName("utf-8"));
        final String converter_tpl = ", converter = SysUserConverter.class";

//        List<String> skipColumn = Arrays.asList("id", "remark", "createTime", "updateTime", "createBy", "updateBy");
        List<String> skipColumn = Arrays.asList();
        for (Map<String, String> map : datas) {
            String column_name = map.get("COLUMN_NAME");

            String columnComment = map.get("COLUMN_COMMENT");

            String columnName = NameUtil.toCamelCase(column_name);
            if (skipColumn.contains(columnName)) {
                continue;
            }

            String dataType = map.get("DATA_TYPE");

            List<String[]> selectList = NameUtil.selectItem(columnComment);

            if (Constants.ID.equalsIgnoreCase(column_name)) {
                String field_id_tmp = field_excel_tpl;
                field_id_tmp = field_id_tmp.replace("{columnName}", columnName);
                field_id_tmp = field_id_tmp.replace("{column_name}", column_name);
                field_id_tmp = field_id_tmp.replace("{columnComment}", columnComment);
                field_id_tmp = field_id_tmp.replace("{dataType}", Config.get(dataType));
                if (Constants.baseFieldSysUserSet.contains(column_name)) {
                	field_id_tmp = field_id_tmp.replace("{converter}", converter_tpl);
				} else {
					field_id_tmp = field_id_tmp.replace("{converter}", "");
				}
                column_field.append(field_id_tmp);
            } else if (!selectList.isEmpty()) {
                List<String> list = Lists.newArrayList();
                for (String[] kv : selectList) {
                    String k = kv[0];
                    String v = kv[1];
                    list.add(String.format("\"%s:%s\"", k, v));
                }
                String autoDescStr = list.stream().collect(Collectors.joining(","));

                String field_simple_select_tmp = field_excel_tpl;
                field_simple_select_tmp = field_simple_select_tmp.replace("{autoDescStr}", autoDescStr);
                field_simple_select_tmp = field_simple_select_tmp.replace("{columnName}", columnName);
                field_simple_select_tmp = field_simple_select_tmp.replace("{column_name}", column_name);
                field_simple_select_tmp = field_simple_select_tmp.replace("{columnComment}", columnComment);
                field_simple_select_tmp = field_simple_select_tmp.replace("{dataType}", Config.get(dataType));
                if (Constants.baseFieldSysUserSet.contains(column_name)) {
                	field_simple_select_tmp = field_simple_select_tmp.replace("{converter}", converter_tpl);
				} else {
					field_simple_select_tmp = field_simple_select_tmp.replace("{converter}", "");
				}
                column_field.append(field_simple_select_tmp);
            } else if (Constants.baseFieldSet.contains(column_name)) {
                String field_create_update_tmp = field_excel_tpl;
                field_create_update_tmp = field_create_update_tmp.replace("{columnName}", columnName);
                field_create_update_tmp = field_create_update_tmp.replace("{column_name}", column_name);
                field_create_update_tmp = field_create_update_tmp.replace("{columnComment}", columnComment);
                field_create_update_tmp = field_create_update_tmp.replace("{dataType}", Config.get(dataType));
				if (Constants.baseFieldSysUserSet.contains(column_name)) {
					field_create_update_tmp = field_create_update_tmp.replace("{converter}", converter_tpl);
				} else {
					field_create_update_tmp = field_create_update_tmp.replace("{converter}", "");
				}
                column_field.append(field_create_update_tmp);
            } else if (Constants.dateSet.contains(dataType)) {
                // 日期框
                String field_simple_tmp = field_excel_tpl;
                field_simple_tmp = field_simple_tmp.replace("{columnName}", columnName);
                field_simple_tmp = field_simple_tmp.replace("{column_name}", column_name);
                field_simple_tmp = field_simple_tmp.replace("{columnComment}", columnComment);
                field_simple_tmp = field_simple_tmp.replace("{dataType}", Config.get(dataType));
                if (Constants.baseFieldSysUserSet.contains(column_name)) {
                	field_simple_tmp = field_simple_tmp.replace("{converter}", converter_tpl);
				} else {
					field_simple_tmp = field_simple_tmp.replace("{converter}", "");
				}
                column_field.append(field_simple_tmp);
            } else {
                // 文本框
                String field_simple_tmp = field_excel_tpl;
                field_simple_tmp = field_simple_tmp.replace("{columnName}", columnName);
                field_simple_tmp = field_simple_tmp.replace("{column_name}", column_name);
                field_simple_tmp = field_simple_tmp.replace("{columnComment}", columnComment);
                field_simple_tmp = field_simple_tmp.replace("{dataType}", Config.get(dataType));
                if (Constants.baseFieldSysUserSet.contains(column_name)) {
                	field_simple_tmp = field_simple_tmp.replace("{converter}", converter_tpl);
				} else {
					field_simple_tmp = field_simple_tmp.replace("{converter}", "");
				}
                column_field.append(field_simple_tmp);
            }
        }

        src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
        src = src.replace("{module}", Config.get("module.name"));
        src = src.replace("{table}", tab);

        String ModelName = NameUtil.dealClassName(tab);
        String modelName = ModelName.substring(0, 1).toLowerCase() + ModelName.substring(1);
        src = src.replace("{column_field}", column_field.toString());
        src = src.replace("{save_form}", save_form.toString());

        src = src.replace("{ModelName}", ModelName);
        src = src.replace("{modelName}", modelName);
        src = src.replace("{key_property}", Constants.ID);

        return src;
    }
}