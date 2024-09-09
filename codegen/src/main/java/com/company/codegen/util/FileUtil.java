package com.company.codegen.util;

import java.io.File;

public class FileUtil {
	private FileUtil() {
	}

	public interface Replace {
		String doReplace(String src);
	}

	public static void generalFromTemplateFile(String templateFile, String destFile, Replace replace) {
		String readUtf8String = cn.hutool.core.io.FileUtil.readUtf8String(new File(templateFile));
		String content = replace.doReplace(readUtf8String);
		File destFileO = new File(destFile);
		if (!destFileO.getParentFile().exists()) {
			destFileO.getParentFile().mkdirs();
		}
		cn.hutool.core.io.FileUtil.writeUtf8String(content, destFileO);
		System.out.println("生成文件:" + destFileO.getPath());
	}
}