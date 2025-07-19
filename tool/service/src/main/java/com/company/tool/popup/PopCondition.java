package com.company.tool.popup;

public interface PopCondition {

	/**
	 * 判断弹窗是否可弹
	 * 
	 * @param popParam
	 * @return 弹窗是否可弹
	 */
	boolean canPop(PopParam popParam);
}