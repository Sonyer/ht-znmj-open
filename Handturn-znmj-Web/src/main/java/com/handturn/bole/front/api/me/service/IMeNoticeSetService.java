package com.handturn.bole.front.api.me.service;

import com.handturn.bole.front.api.me.vo.InitMeNoticeSetResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通知设置
 */
public interface IMeNoticeSetService {
    /**
     * 通知设置
     * @param request
     * @return
     */
    List<InitMeNoticeSetResponseVo> initMeNoticeSet(HttpServletRequest request);

}
