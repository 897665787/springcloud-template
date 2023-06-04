package com.company.admin.controller.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.company.framework.context.HttpContextUtil;

public abstract class SelectPageUtil {

	private SelectPageUtil() {
	}

	public static <T> Page<T> page() {
		int current = HttpContextUtil.parameterInt("page", 1);
		int size = HttpContextUtil.parameterInt("limit", 10);
		Page<T> pageParam = new Page<>(current, size);
		return pageParam;
	}
}
