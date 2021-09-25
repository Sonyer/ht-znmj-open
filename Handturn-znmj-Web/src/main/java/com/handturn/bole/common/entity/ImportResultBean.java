package com.handturn.bole.common.entity;

import lombok.Data;

@Data
public class ImportResultBean {
    // 错误文件名称
    private String errorFileName;
    // 是否成功
    private boolean success;
    // 返回信息
    private String returnInfo;
    // 是否系统异常
    private boolean systemError;

    private String systemReturnInfo;

    private String fileName;

    private String filePath;

    private String fileRequestUrl;

    private Long fileSize;

    private Integer imgWidth;

    private Integer imgHeight;

    private String oldFileName;


}
