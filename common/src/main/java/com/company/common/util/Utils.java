package com.company.common.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.crypto.SecureUtil;

public class Utils {
	
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String md5(String data) {
		return SecureUtil.md5(data);
	}
	
	/**
	 * 以/拼接remark(取左侧200字符)
	 * 
	 * @param oldRemark
	 * @param newRemark
	 * @return
	 */
	public static String appendRemark(String oldRemark, String newRemark) {
		if (StringUtils.isBlank(newRemark)) {
			return oldRemark;
		}
		String remark = newRemark;
		if (StringUtils.isNotBlank(oldRemark)) {
			remark = StringUtils.join(new String[] { oldRemark, remark }, "/");
		}
		remark = StringUtils.substring(remark, 0, 200);
		return remark;
	}
	
	/**
	 * 以/拼接remark(取右侧200字符)
	 * 
	 * @param oldRemark
	 * @param newRemark
	 * @return
	 */
	public static String rightRemark(String oldRemark, String newRemark) {
		if (StringUtils.isBlank(newRemark)) {
			return oldRemark;
		}
		String remark = newRemark;
		if (StringUtils.isNotBlank(oldRemark)) {
			remark = StringUtils.join(new String[] { oldRemark, remark }, "/");
		}
		remark = StringUtils.right(remark, 200);
		return remark;
	}

	/**
	 * 任意一个strs中的值包含searchStr
	 * 
	 * @param searchStr
	 * @param strs
	 * @return
	 */
	public static boolean anyContains(String searchStr, String... strs) {
		if (strs == null) {
			return false;
		}
		for (String str : strs) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			if (str.contains(searchStr)) {
				return true;
			}
		}
		return false;
	}
}
