package com.handturn.bole.front.api.me.service;

import com.handturn.bole.front.api.me.vo.InitMeNoticeResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 我的通知
 */
public interface IMeNoticeService {
    /**
     * 获取我的信息
     * @param request
     * @return
     */
    List<InitMeNoticeResponseVo> initMeNotice(HttpServletRequest request, Integer pageIndex);

}
