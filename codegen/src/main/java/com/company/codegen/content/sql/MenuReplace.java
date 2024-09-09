package com.company.codegen.content.sql;

import java.util.List;
import java.util.Map;

import com.company.codegen.db.DBUtil;
import com.company.codegen.util.Config;
import com.company.codegen.util.FileUtil.Replace;
import com.company.codegen.util.NameUtil;

public class MenuReplace implements Replace {
	
	List<Map<String, String>> datas = null;
	String tab;
	
	public MenuReplace(String tab){
		datas = DBUtil.getColumnsByTable(tab);
		this.tab = tab;
	}

	@Override
	public String doReplace(String src) {
		src = src.replace("{module_name}", DBUtil.getCommentByTable(tab));
		src = src.replace("{module}", Config.get("module.name"));
		src = src.replace("{module.name.zh}", Config.get("module.name.zh"));
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(tab.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		
		src = src.replace("{ModelName}", ModelName);
		src = src.replace("{modelName}", modelName);
		
		return src;
	}
}