package com.company.admin.beetl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

public class GlobalExt implements WebRenderExt {
	static long version = System.currentTimeMillis();

	@Override
	public void modify(Template template, GroupTemplate gt, HttpServletRequest request, HttpServletResponse response) {
		// js,css 的版本编号
		// 每次请求都会进入
		template.binding("version", version);
	}
}
