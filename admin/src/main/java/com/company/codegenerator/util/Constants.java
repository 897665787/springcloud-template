package com.company.codegenerator.util;

import java.util.HashSet;
import java.util.Set;

public class Constants {
	public final static String ID = "id";
	
	public static Set<String> numberSet = new HashSet<String>();
	public static Set<String> dateSet = new HashSet<String>();
	public static Set<String> ignoreFieldSet = new HashSet<String>();

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
		ignoreFieldSet.add("update_time");
	}
}