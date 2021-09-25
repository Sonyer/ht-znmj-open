package com.handturn.bole.front.api.bas.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用相关
 */
public interface IFrontBasService {

    /**
     * 验证码
     * @param request
     * @param phoneNumber
     * @return
     */
    Boolean validateCode(HttpServletRequest request, String phoneNumber);



}
