package com.company.tool.file.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 本地磁盘客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class LocalClient {
	private String endpoint = null;

	public LocalClient(String endpoint) {
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {
		// bucketName->根目录
		File resultFile = FileUtil.writeFromStream(input,
				endpoint + File.separator + bucketName + File.separator + key);

		// 文件URL的格式为endpoint/bucketName/key
		String url = resultFile.getAbsolutePath();
		log.info("url:{}", url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		InputStream inputStream = FileUtil
				.getInputStream(endpoint + File.separator + bucketName + File.separator + key);
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		FileUtil.del(endpoint + File.separator + bucketName + File.separator + key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "D:/home/data/oss";
		LocalClient client = new LocalClient(endpoint);

		String bucketName = "buket-template-dev";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = client.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
//		InputStream download = client.getObject(bucketName, key);
//		FastByteArrayOutputStream read = IoUtil.read(download);
//		OutputStream out = new FileOutputStream(new File("D:/local-download.jpg"));
//		IoUtil.write(out, true, read.toByteArray());

		// 删除文件
//		client.deleteObject(bucketName, key);

	}
}