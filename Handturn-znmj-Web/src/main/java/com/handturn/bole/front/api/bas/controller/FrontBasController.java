package com.handturn.bole.front.api.bas.controller;

import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.bas.service.IFrontBasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 邻林购-微信登陆 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/bas")
public class FrontBasController {

    @Autowired
    private IFrontBasService frontBasService;

    @PostMapping("validateCode")
    @ResponseBody
    public FebsResponse validateCode(HttpServletRequest request, String phoneNumber) {
        Boolean flag = frontBasService.validateCode(request,phoneNumber);
        return new FebsResponse().success().data(flag);
    }
}
