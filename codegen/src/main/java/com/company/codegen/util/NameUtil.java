package com.company.codegen.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.company.common.util.JsonUtil;

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
		
		String table_prefix = Config.get("table_prefix");
		String modelName = NameUtil.toCamelCase(name.replaceFirst(table_prefix, ""));
		String ModelName = NameUtil.upperFirst(modelName);
		return ModelName;
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
	public static String columnComment(String columnComment) {
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
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", ",", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", "，", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", ",", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", "，", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", " ", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "(", ")", " ", "：");
		}
		//
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", ",", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", "，", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", ",", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", "，", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", " ", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "（", "）", " ", "：");
		}
		//
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", ",", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", "，", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", ",", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", "，", "：");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", " ", ":");
		}
		if (selectList.isEmpty()) {
			selectList = selectItem(columnComment, "<", ">", " ", "：");
		}
		return selectList;
	}

	private static List<String[]> selectItem(String columnComment, String start, String end, String spilt, String mid) {
		List<String[]> selectList = Lists.newArrayList();
		if (columnComment.contains(start) && columnComment.contains(end)) {
			int indexS = columnComment.indexOf(start);
			int indexE = columnComment.lastIndexOf(end);
			String strSE = columnComment.substring(indexS + 1, indexE);
			if (strSE.contains(spilt) && strSE.contains(mid)) {
				String[] itemkvs = strSE.split(spilt);
				for (String itemkv : itemkvs) {
					String[] kv = itemkv.split(mid);
					selectList.add(kv);
				}
			}
		}
		return selectList;
	}

	public static void main(String[] args) {
		String[] selectItemDemos = new String[] { //
				"状态(ON:正常,OFF:停用)", //
				"状态(ON:正常 OFF:停用)", //
				"状态(ON：正常 OFF：停用)", //
				"状态（ON：正常，OFF：停用）", //
				"数据范围(1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限)", //
				"性别(0:男,1:女,2:未知)", //
				"性别(0:男 1:女 2:未知)",//
				"性别<0:男 1:女 2:未知>",//
		};

		for (String selectItemDemo : selectItemDemos) {
			System.out.println(JsonUtil.toJsonString(NameUtil.selectItem(selectItemDemo)));
		}
	}
}
