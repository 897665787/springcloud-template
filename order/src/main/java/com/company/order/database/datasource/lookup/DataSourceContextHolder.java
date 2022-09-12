package com.company.order.database.datasource.lookup;

public class DataSourceContextHolder {

	private static final ThreadLocal<String> DATASOURCE_CONTEXT_HOLDER = new ThreadLocal<String>();

	/**
	 * 设置数据源
	 */
	public static void setDataSourceName(String sourceName) {
		DATASOURCE_CONTEXT_HOLDER.set(sourceName);
	}

	/**
	 * 获取数据源
	 */
	public static String getDataSourceName() {
		return DATASOURCE_CONTEXT_HOLDER.get();
	}

	/**
	 * 清除数据源
	 */
	public static void clearDataSourceName() {
		DATASOURCE_CONTEXT_HOLDER.remove();
	}
}
