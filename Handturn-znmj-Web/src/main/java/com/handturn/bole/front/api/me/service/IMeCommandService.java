package com.handturn.bole.front.api.me.service;

import com.handturn.bole.front.api.me.vo.MeSubmitCommandResponseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的 管理口令
 */
public interface IMeCommandService {
    /**
     * 提交口令
     * @param request
     * @return
     */
    void subimitCommand(HttpServletRequest request, MeSubmitCommandResponseVo requestVo);

}
