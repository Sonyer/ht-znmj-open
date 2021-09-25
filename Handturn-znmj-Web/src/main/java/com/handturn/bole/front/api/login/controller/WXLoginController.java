package com.handturn.bole.front.api.login.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.login.service.IWXLoginService;
import com.handturn.bole.front.api.login.vo.WXLoginResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信登陆 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/wechat")
public class WXLoginController {

    @Autowired
    private IWXLoginService wxLoginService;

    @PostMapping("login")
    @ResponseBody
    public FebsResponse login(HttpServletRequest request, String userInfo, String wxCode, String encryptedData, String iv, String longitude, String latitude) {
        WXLoginResponseVo wXLoginResponseVo =  wxLoginService.login(request,userInfo,wxCode,encryptedData,iv,longitude,latitude);
        return new FebsResponse().success().data(wXLoginResponseVo);
    }

    @PostMapping("getPhoneNumber")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse getPhoneNumber(HttpServletRequest request, String wxCode, String encryptedData, String iv) {
        WXLoginResponseVo wXLoginResponseVo =  wxLoginService.getPhoneNumber(request,wxCode,encryptedData,iv);
        return new FebsResponse().success().data(wXLoginResponseVo);
    }

    @PostMapping("tokenVerify")
    @ResponseBody
    public FebsResponse tokenVerify(HttpServletRequest request, String accountCode, String token) {
        Boolean flag = wxLoginService.tokenVerify(accountCode,token);
        return new FebsResponse().success().data(flag);
    }



}
