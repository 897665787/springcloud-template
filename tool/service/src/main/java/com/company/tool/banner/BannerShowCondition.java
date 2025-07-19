package com.company.tool.banner;

public interface BannerShowCondition {
	String PREFIX = "banner-";// bean命名前缀

	/**
	 * 判断轮播图是否可显示
	 * 
	 * @param showParam
	 * @return 轮播图是否可显示
	 */
	boolean canShow(ShowParam showParam);
}