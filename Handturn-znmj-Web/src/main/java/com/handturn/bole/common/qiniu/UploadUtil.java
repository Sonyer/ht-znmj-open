package com.handturn.bole.common.qiniu;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件上传接口
 */
public interface UploadUtil {
    String uploadFile(MultipartFile multipartFile);

    String uploadFile(String filePath, MultipartFile multipartFile);

    String uploadFile(MultipartFile multipartFile, String fileName);

    String uploadFile(MultipartFile multipartFile, String fileName, String filePath);

    String uploadFile(File file);

    String uploadFile(String filePath, File file);

    String uploadFile(File file, String fileName);

    String uploadFile(File file, String fileName, String filePath);

    String uploadFile(byte[] data);

    String uploadFile(String filePath, byte[] data);

    String uploadFile(byte[] data, String fileName);

    String uploadFile(byte[] data, String fileName, String filePath);
}
