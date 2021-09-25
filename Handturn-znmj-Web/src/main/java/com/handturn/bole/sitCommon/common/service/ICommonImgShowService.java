package com.handturn.bole.sitCommon.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统-组织 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
public interface ICommonImgShowService{
    /**
     * 图片展现
     * @return
     */
    void imgShow(String typeName, String dateStr, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 文件展现
     * @return
     */
    void fileShow(String typeName, String dateStr, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
