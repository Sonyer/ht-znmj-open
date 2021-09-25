package com.handturn.bole.front.api.home.controller;

import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.home.service.IHomeService;
import com.handturn.bole.front.api.home.vo.AuthAreaPageQueryResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/home")
public class HomeController {

    @Autowired
    private IHomeService homeService;

    @PostMapping("authAreaPageQuery")
    @ResponseBody
    public FebsResponse authAreaPageQuery(HttpServletRequest request, String pageIndex, String searchValue) {
        String curAccountCode = request.getHeader("accountCode");
        AuthAreaPageQueryResponseVo authAreaPgeQueryResponseVos =  homeService.authAreaPageQuery(pageIndex,searchValue,curAccountCode);
        return new FebsResponse().success().data(authAreaPgeQueryResponseVos);
    }
}
