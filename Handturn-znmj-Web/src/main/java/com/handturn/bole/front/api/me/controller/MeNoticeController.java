package com.handturn.bole.front.api.me.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.me.service.IMeNoticeService;
import com.handturn.bole.front.api.me.vo.InitMeNoticeResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 我的通知 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/me")
public class MeNoticeController {

    @Autowired
    private IMeNoticeService meNoticeService;

    @PostMapping("initMeNotice")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse initMeNotice(HttpServletRequest request, Integer pageIndex) {
        List<InitMeNoticeResponseVo> resultVos =  meNoticeService.initMeNotice(request,pageIndex);
        return new FebsResponse().success().data(resultVos);
    }

}
