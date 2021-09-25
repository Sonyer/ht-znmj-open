package com.handturn.bole.front.api.me.service;

import com.handturn.bole.front.api.me.vo.MeVisitorAuditResponseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 访客审核
 */
public interface IMeVisitorAuditService {
    /**
     * 获取我的浏览历史
     * @param request
     * @return
     */
    MeVisitorAuditResponseVo initVisitorAudit(HttpServletRequest request, Integer pageIndex, String currentStatusType);

    /**
     * 收货确认
     * @param request
     * @return
     */
    String auditVisitor(HttpServletRequest request, String visitorApplyId);


    /**
     * 退货(款)申请
     * @param request
     * @return
     */
    String cancelVisitor(HttpServletRequest request, String visitorApplyId);

}
