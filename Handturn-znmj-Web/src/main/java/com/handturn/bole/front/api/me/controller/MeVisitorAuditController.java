package com.handturn.bole.front.api.me.controller;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.front.api.me.service.IMeVisitorAuditService;
import com.handturn.bole.front.api.me.vo.MeVisitorAuditResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 邻林购-我的购买订单 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/visitorAudit")
public class MeVisitorAuditController {

    @Autowired
    private IMeVisitorAuditService meVisitorAuditService;

    @PostMapping("initVisitorAudit")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse initVisitorAudit(HttpServletRequest request, Integer pageIndex, String currentStatusType) {
        MeVisitorAuditResponseVo resultVos =  meVisitorAuditService.initVisitorAudit(request,pageIndex,currentStatusType);
        return new FebsResponse().success().data(resultVos);
    }

    @PostMapping("auditVisitor")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse auditVisitor(HttpServletRequest request, String visitorApplyId) {
        String resultStatus =  meVisitorAuditService.auditVisitor(request,visitorApplyId);
        return new FebsResponse().success().data(resultStatus);
    }

    @PostMapping("cancelVisitor")
    @ResponseBody
    @NeedTokenVerificationEndpoint
    public FebsResponse cancelVisitor(HttpServletRequest request, String visitorApplyId) {
        String resultStatus =  meVisitorAuditService.cancelVisitor(request,visitorApplyId);
        return new FebsResponse().success().data(resultStatus);
    }

}
