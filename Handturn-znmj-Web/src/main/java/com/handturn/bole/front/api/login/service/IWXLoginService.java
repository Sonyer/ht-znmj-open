package com.handturn.bole.front.api.login.service;

import com.handturn.bole.front.api.login.vo.WXLoginResponseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信相关
 */
public interface IWXLoginService {
    /**
     * 微信小程序，直接认证登陆
     * @param request
     * @param wxCode
     * @param encryptedData
     * @param iv
     * @return
     */
    WXLoginResponseVo login(HttpServletRequest request, String userInfo, String wxCode, String encryptedData, String iv, String longitude, String latitude);

    /**
     * 微信小程序，获取手机号码
     * @param request
     * @param wxCode
     * @param encryptedData
     * @param iv
     * @return
     */
    WXLoginResponseVo getPhoneNumber(HttpServletRequest request, String wxCode, String encryptedData, String iv);

    /**
     * 校验登陆授权
     * @return
     */
    boolean tokenVerify(String accountCode, String token);
}
