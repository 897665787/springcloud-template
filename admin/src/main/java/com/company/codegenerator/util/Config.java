package com.company.codegenerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class Config {
	private static Properties prop = new Properties();

	static {
		InputStream is = null;
		try {
			File file = new File("src/main/java/com/company/codegenerator");
			String canonicalPath = file.getCanonicalPath();
			System.out.println("配置文件generate.properties所在目录:"+canonicalPath);
			
			File propFile = new File(new File(canonicalPath),"generate.properties");
			is = new FileInputStream(propFile);
//			prop.load(is);
			prop.load(new InputStreamReader(is, "utf-8"));
			System.out.println("加载配置文件generate.properties成功");
		} catch (IOException e) {
			System.out.println("加载配置文件generate.properties出错");
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Config() {
	}

	public static String get(String key) {
		return prop.getProperty(key);
	}
}
