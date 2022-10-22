package com.company.admin.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.company.admin.util.EmojiCharacterUtil;

/**
 * 小梭静态标签
 * Created by JQ棣 on 2018/11/10.
 */
public class XSStaticTag extends SimpleTagSupport {

	/**
	 * 解析可见字符标识字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String reverseEmoji(String src) {
		return EmojiCharacterUtil.reverse(src);
	}
	
}
