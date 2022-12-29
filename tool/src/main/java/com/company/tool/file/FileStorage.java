package com.company.tool.file;

import java.io.InputStream;

public interface FileStorage {
	FileUrl upload(InputStream inputStream, String fileName);

	InputStream download(String fileName);

	void remove(String fileName);
}