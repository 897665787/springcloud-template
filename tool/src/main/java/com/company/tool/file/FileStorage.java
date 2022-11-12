package com.company.tool.file;

import java.io.InputStream;

public interface FileStorage {
	String upload(InputStream inputStream, String fileName);

	InputStream download(String fileName);

	void remove(String fileName);
}