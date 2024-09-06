package com.company.tool.innercallback.strategy;

/**
 * 秒数策略
 * 
 * @author JQ棣
 *
 */
public interface SecondsStrategy {

	/**
	 * 
	 * @param increaseSeconds
	 *            递增秒数
	 * @param failure
	 *            当前失败次数
	 * @return 下次执行增加的秒数
	 */
	int nextSeconds(int increaseSeconds, int failure);
}
