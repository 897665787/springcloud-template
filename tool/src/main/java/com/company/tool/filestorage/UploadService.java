package com.company.tool.filestorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传
 */
@Slf4j
@Service
public class UploadService {

	@Autowired
	private FileStorage fileStorage;

	/**
	 * <pre>
	 * 上传图片
	 * （自动生成image/202201/01/00033d9fea0b484eac1509567e87e61a.jpg格式的文件名）
	 * </pre>
	 *
	 * @param outUrl
	 *            外部图片访问链接,如：https://tfs.alipayobjects.com/images/partner/TB1ynYQXmCwMeJk6XeaXXa9rpXa
	 * @return 图片的访问链接
	 */
	public String uploadImg(String outImgUrl) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HttpUtil.download(outImgUrl, out, true);
		byte[] bytes = out.toByteArray();

		return uploadImg(bytes);
	}

	/**
	 * <pre>
	 * 上传图片
	 * （自动生成image/202201/01/00033d9fea0b484eac1509567e87e61a.jpg格式的文件名）
	 * </pre>
	 *
	 * @param bytes
	 *            文件流的byte数组
	 * @return 图片的访问链接
	 */
	public String uploadImg(byte[] bytes) {
		return uploadImg(bytes, String.format("%s.%s", "uuid-replace", Utils.extraSuffix(bytes)));
	}

	/**
	 * <pre>
	 * 上传图片
	 * （自动生成image/202201/01/00033d9fea0b484eac1509567e87e61a.jpg格式的文件名）
	 * </pre>
	 *
	 * @param bytes
	 *            文件流的byte数组
	 * @param fileName
	 *            文件名
	 * @return 图片的访问链接
	 */
	public String uploadImg(byte[] bytes, String fileName) {
		if (StringUtils.isBlank(fileName)) {
			fileName = String.format("%s.%s", "uuid-replace", Utils.extraSuffix(bytes));
		}
		String generateFileName = generateFileName("image", fileName);
		log.info("fileName:{}, generateFileName:{}", fileName, generateFileName);
		
		try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
			FileUrl fileUrl = fileStorage.upload(inputStream, generateFileName);
			log.info("fileUrl:{}", JsonUtil.toJsonString(fileUrl));
			return fileUrl.getDomainUrl();
		} catch (IOException e) {
			log.error("IOException", e);
			throw new BusinessException("上传失败");
		}
	}
	
	/**
	 * <pre>
	 * 上传图片
	 * （自动生成image/202201/01/00033d9fea0b484eac1509567e87e61a.jpg格式的文件名）
	 * </pre>
	 *
	 * @param inputStream
	 *            文件流
	 * @param fileName
	 *            文件名
	 * @return 图片的访问链接
	 */
	public String uploadImg(InputStream inputStream, String fileName) {
		if (StringUtils.isBlank(fileName)) {
			byte[] bytes = IoUtil.readBytes(inputStream);
			fileName = String.format("%s.%s", "uuid-replace", Utils.extraSuffix(bytes));
		}
		String generateFileName = generateFileName("image", fileName);
		log.info("fileName:{}, generateFileName:{}", fileName, generateFileName);
		
		FileUrl fileUrl = fileStorage.upload(inputStream, generateFileName);
		log.info("fileUrl:{}", JsonUtil.toJsonString(fileUrl));
		return fileUrl.getDomainUrl();
	}
	
	/**
	 * 上传文件
	 *
	 * @param bytes
	 *            文件流的byte数组
	 * @param fileName
	 *            文件名
	 * @return 文件的访问链接
	 */
	public String upload(byte[] bytes, String fileName) {
		if (StringUtils.isBlank(fileName)) {
			fileName = String.format("%s.%s", IdUtil.fastSimpleUUID(), Utils.extraSuffix(bytes));
		}
		log.info("fileName:{}", fileName);
		
		try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
			FileUrl fileUrl = fileStorage.upload(inputStream, fileName);
			log.info("fileUrl:{}", JsonUtil.toJsonString(fileUrl));
			return fileUrl.getDomainUrl();
		} catch (IOException e) {
			log.error("IOException", e);
			throw new BusinessException("上传失败");
		}
	}
	
	/**
	 * 生成文件名（目录+文件名，例如:basePath/202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
	 * 
	 * @param basePath
	 * @param fileName
	 * @return
	 */
	private static String generateFileName(String basePath, String fileName) {
		String generateFileName = generateFileName(fileName);
		// 生成文件名（目录+文件名，例如:basePath/202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
		if (basePath == null) {
			basePath = "";
		}
		if (basePath.startsWith("/")) {
			basePath = basePath.substring(1, basePath.length());
		}
		if (StringUtils.isNotBlank(basePath) && !basePath.endsWith("/")) {
			basePath = basePath + "/";
		}
		return basePath + generateFileName;
	}
	
	/**
	 * 生成文件名（例如:202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
	 * 
	 * @param fileName
	 * @return
	 */
	private static String generateFileName(String fileName) {
		// 生成文件名（目录+文件名，例如:basePath/202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);

		String ext = null;
		String[] fileNameSplit = fileName.split("\\.");
		if (fileNameSplit.length > 1) {
			ext = fileNameSplit[fileNameSplit.length - 1];
		}
		return String.format("%d%02d/%02d/%s.%s", year, month, day, IdUtil.fastSimpleUUID(), ext);
	}

	/**
	 * 获取文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public String getContent(String fileName) {
		InputStream download = fileStorage.download(fileName);
		String content = IoUtil.read(download, Charset.forName("utf-8"));
		return content;
	}
	
	public static void main(String[] args) {
		String fileName = "uuid-replace.jpg";
		
		// 2023/07-19/0b0fbf3ecb6c41ba96b6e20171a2a78d.jpg
		System.out.println(generateFileName(fileName));
		
		// image/2023/07-19/0b0fbf3ecb6c41ba96b6e20171a2a78d.jpg
		System.out.println(generateFileName("image", fileName));
		System.out.println(generateFileName("/image", fileName));
		System.out.println(generateFileName("image/", fileName));
		System.out.println(generateFileName("/image/", fileName));
		
		String filePath = "D:/111.jpg";
//		String filePath = "D:/guohui.jpg";
//		String filePath = "D:/图片.rar";
		byte[] bytes = FileUtil.readBytes(filePath);
		System.out.println(Utils.extraSuffix(bytes));
		
//		String outUrl = "https://tfs.alipayobjects.com/images/partner/TB1ynYQXmCwMeJk6XeaXXa9rpXa";
		String outUrl = "https://thirdwx.qlogo.cn/mmopen/vi_32/zRziaYdcx2ib9icsCUSsAmqdhia9bLoCwHRHr02icZAI8DA5n97AVpdwF3ziafzJ0ViaQXia4ibGSAMSUKR1swgufMtS1MA/132";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HttpUtil.download(outUrl, out, true);
		System.out.println(Utils.extraSuffix(out.toByteArray()));
	}
}