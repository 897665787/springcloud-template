package com.company.tool.nav;

public interface NavShowCondition {
	String PREFIX = "nav-";// bean命名前缀

	/**
	 * 判断金刚位是否可显示
	 * 
	 * @param showParam
	 * @return 金刚位是否可显示
	 */
	boolean canShow(ShowParam showParam);
}