package com.company.admin.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by gustinlau on 10/30/17.
 */
public interface OssService {
    /**
     * 图片上传
     *
     * @param folders  文件夹名称数组
     * @param files    文件数组
     * @param withSize 返回URL是否带图片信息(长宽)
     * @return URL字符串数组
     */
    List<String> imageUpload(String folders[], MultipartFile[] files, Boolean withSize);

    /**
     * 图片上传
     *
     * @param folders 文件夹名称数组
     * @param files   文件数组
     * @return URL字符串数组（不带图片信息）
     */
    List<String> imageUpload(String folders[], MultipartFile[] files);

    /**
     * 文件上传
     *
     * @param folders 文件夹名称数组
     * @param files   文件数组
     * @return URL字符串数组
     */
    List<String> fileUpload(String folders[], MultipartFile[] files);

    /**
     * 文件上传
     *
     * @param folder 文件夹名称
     * @param files  文件数组
     * @return URL字符串数组
     */
    List<String> fileUpload(String folder, MultipartFile[] files);

    /**
     * 下载文件
     *
     * @param fileName 在oss中的名称
     * @param out      输出流
     */
    void fileDownload(String fileName, OutputStream out) throws IOException;
}
