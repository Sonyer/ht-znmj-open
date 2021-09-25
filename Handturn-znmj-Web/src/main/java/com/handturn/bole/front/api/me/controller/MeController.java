package com.handturn.bole.front.api.me.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.interceptcontent.InterceptContentUntil;
import com.handturn.bole.front.api.me.service.IMeService;
import com.handturn.bole.front.api.me.vo.InitMeResponseVo;
import com.handturn.bole.master.common.service.IWxAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("sitapi/me")
public class MeController {

    @Autowired
    private IMeService meService;
    @Autowired
    private IWxAccessTokenService wxAccessTokenService;

    @PostMapping("initMe")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse initMe(HttpServletRequest request) {
        InitMeResponseVo resultVo =  meService.initMe(request);
        return new FebsResponse().success().data(resultVo);
    }

    @PostMapping("updateNickName")
    @ResponseBody
    public FebsResponse updateNickName(HttpServletRequest request, String nickName) {
        Boolean result = meService.updateNickName(request,nickName);
        return new FebsResponse().success().data(result);
    }

    @ResponseBody
    @PostMapping("/uploadMeAvatar")
    public FebsResponse uploadMeAvatar (@RequestParam(value = "file", required = false) MultipartFile file, String accountCode, String token) {
        //校验图片是否违规
        String accessToken = wxAccessTokenService.getAccessToken4Wx();
        Boolean flag = InterceptContentUntil.checkPic(file,accessToken);
        if(flag == true){
            String imgRequest = meService.uploadMeAvatar(file,accountCode,token);
            return new FebsResponse().success().data(imgRequest);
        }else{
            throw new FebsException("请检查你的图片是否涉黄、暴力，或其他违规行为。");
        }

    }

}
