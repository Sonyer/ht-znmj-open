package com.handturn.bole.front.api.visitorApply.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.visitorApply.service.IVisitorApplyService;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplyPageQueryResponseVo;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplySubmitRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/visitorApply")
public class VisitorApplyController {

    @Autowired
    private IVisitorApplyService visitorApplyService;

    @PostMapping("initVisitorApply")
    @ResponseBody
    public FebsResponse initVisitorApply(HttpServletRequest request,String orgCode,String ocCode,String authAreaCode) {
        String curAccountCode = request.getHeader("accountCode");
        VisitorApplyPageQueryResponseVo visitorApplyPageQueryResponseVo =  visitorApplyService.initVisitorApply(curAccountCode,orgCode,ocCode,authAreaCode);
        return new FebsResponse().success().data(visitorApplyPageQueryResponseVo);
    }

    @ResponseBody
    @PostMapping("/uploadVisitorImg")
    //@NeedTokenVerificationEndpoint 这里使用uniapp组件不能使用token
    public FebsResponse uploadVisitorImg (@RequestParam(value = "file", required = false) MultipartFile file, String accountCode, String token) {
        String imgurl = visitorApplyService.uploadVisitorImg(file,accountCode,token);
        return new FebsResponse().success().data(imgurl);
    }

    @PostMapping("submitVisitorApply")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse submitVisitorApply(HttpServletRequest request, VisitorApplySubmitRequestVo requestVo) {
        Boolean result =  visitorApplyService.submitVisitorApply(request,requestVo);
        return new FebsResponse().success().data(result);
    }
}
