package com.handturn.bole.front.api.me.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.me.service.IMeCommandService;
import com.handturn.bole.front.api.me.vo.MeSubmitCommandResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 邻林购-认证申请 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/me")
public class MeCommandController {

    @Autowired
    private IMeCommandService meCommandService;

    @PostMapping("subimitCommand")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse subimitCommand(HttpServletRequest request, MeSubmitCommandResponseVo requestVo) {
        meCommandService.subimitCommand(request,requestVo);
        return new FebsResponse().success();
    }


}
