package com.company.codegenerator.util;

import java.util.List;

import com.google.common.collect.Lists;

import cn.hutool.core.util.StrUtil;

public final class NameUtil {
	private NameUtil() {
	}

	public static String toCamelCase(String name) {
		return StrUtil.toCamelCase(name, '_');
	}
	
	public static String upperFirst(String name) {
		return StrUtil.upperFirst(name);
	}
	
	public static String dealClassName(String name) {
//		StrUtil.toCamelCase("code_generator", '_');
		
		if(!name.contains("_")){
			name = "_" + name;
		}
		return dealName(name, 1);
	}

	public static String dealGetSetFieldName(String name) {
		return dealName("_" + name, 0);
	}

	private static String dealName(String str, int ignoreFor_) {
		for (int i = 0; i < ignoreFor_; i++) {
			str = str.substring(str.indexOf("_"));
		}
		String[] chars = str.split("");
		boolean leap = false;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals("_")) {
				leap = true;
			} else if (leap) {
				chars[i] = String.valueOf(chars[i]).toUpperCase();
				leap = false;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String string : chars) {
			sb.append(string);
		}
		return sb.toString().replace("_", "");
	}
	
	/**
	 * demo
	 * 
	 * <pre>
	 * 审核状态(1:待审核,21:通过,22:驳回)
	 * </pre>
	 * 
	 * @param columnComment
	 * @return 审核状态
	 */
	public static String columnName(String columnComment) {
		String columnName = columnComment;
		int index = -1;
		if(index == -1 && columnComment.contains("(") && columnComment.contains(")")){
			index = columnComment.indexOf("(");
		}
		if(index == -1 && columnComment.contains("（") && columnComment.contains("）")){
			index = columnComment.indexOf("（");
		}
		if(index == -1 && columnComment.contains("<") && columnComment.contains(">")){
			index = columnComment.indexOf("<");
		}
		if(index == -1){
			index = columnComment.indexOf(",");
		}
		if(index == -1){
			index = columnComment.indexOf("，");
		}
		if(index == -1){
			index = columnComment.indexOf(" ");
		}
		if(index != -1){
			columnName = columnComment.substring(0, index);
		}
		return columnName;
	}

	/**
	 * demo
	 * 
	 * <pre>
	 * 审核状态(1:待审核,21:通过,22:驳回)
	 * </pre>
	 * 
	 * @param columnComment
	 * @return 1:待审核,21:通过,22:驳回
	 */
	public static List<String[]> selectItem(String columnComment) {
		List<String[]> selectList = Lists.newArrayList();
		if (columnComment.contains("(") && columnComment.contains(")")) {
			int indexS = columnComment.indexOf("(");
			int indexE = columnComment.lastIndexOf(")");
			String strSE = columnComment.substring(indexS + 1, indexE);
			if (strSE.contains(":")) {
				String[] itemkvs = strSE.split(",");
				for (String itemkv : itemkvs) {
					String[] kv = itemkv.split(":");
					selectList.add(kv);
				}
			}
		}
		return selectList;
	}
	
}
