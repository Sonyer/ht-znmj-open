package com.handturn.bole.common.qiniu;

import com.qiniu.util.Auth;

public class UploadFactory {
    public static UploadUtil createUpload(String accessKey, String secretKeySpec, String bucketHostName, String bucketName) {
        Auth auth = Auth.create(accessKey, secretKeySpec);
        return new QiniuUtil(bucketHostName, bucketName, auth);
    }
}
