package com.company.codegen.util;

import java.util.HashSet;
import java.util.Set;

public class Constants {
	public final static String ID = "id";
	
	public static Set<String> numberSet = new HashSet<String>();
	public static Set<String> dateSet = new HashSet<String>();
	public static Set<String> ignoreFieldSet = new HashSet<String>();
	public static Set<String> baseFieldSet = new HashSet<String>();
	public static Set<String> baseFieldSysUserSet = new HashSet<String>();

	static {
		numberSet.add("bigint");
		numberSet.add("smallint");
		numberSet.add("tinyint");
		numberSet.add("int");
		numberSet.add("decimal");
		numberSet.add("double");
		numberSet.add("float");

		dateSet.add("date");
		dateSet.add("datetime");
		dateSet.add("timestamp");

		ignoreFieldSet.add("create_time");
		ignoreFieldSet.add("create_by");
		ignoreFieldSet.add("update_time");
		ignoreFieldSet.add("update_by");
		
		baseFieldSet.add("id");
		baseFieldSet.add("remark");
		baseFieldSet.add("create_time");
		baseFieldSet.add("create_by");
		baseFieldSet.add("update_time");
		baseFieldSet.add("update_by");
		
		baseFieldSysUserSet.add("create_by");
		baseFieldSysUserSet.add("update_by");
	}
}