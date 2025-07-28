package com.company.framework.constant;

public interface CommonConstants {
	String BASE_PACKAGE = "com.company";

	/**
	 * 过滤器优先级
	 */
	public interface FilterOrdered {
		// 值越小，优先级越高
		int MDC = -10;
		int SUMMARY_API = -5;
		int HTTPCONTEXT = 5;
		int DEVICE = 6;
		int SOURCE = 7;
		int REQUEST = 10;
		int TOKEN = 30;
		int USERCONTEXT = 35;
	}

	/**
	 * 拦截器优先级
	 */
	public interface InterceptorOrdered {
		// 值越小，优先级越高

		// edge
		int ACCESS_CONTROL = 1;

		// admin 引用了edge
		int PERMISSION = 5;

		// openapi
		int SIGN = 1;
	}

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单树根节点
	 */
	Long MENU_TREE_ROOT_ID = -1L;

	/**
	 * 菜单
	 */
	String MENU = "0";

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * JSON 资源
	 */
	String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "pig-ui";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "pig";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;

	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 验证码前缀
	 */
	String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

	/**
	 * 当前页
	 */
	String CURRENT = "current";

	/**
	 * size
	 */
	String SIZE = "size";

}
