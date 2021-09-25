package com.handturn.bole.master.common.service;

import com.handturn.bole.common.entity.FebsConstant;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 微信通知服务
 */
public interface IWxNoticeService {

    /**
     * 微信通知服务
     * @param memeberId
     * @param templateId
     * @param mapData
     * @return
     */
    @Async(FebsConstant.ASYNC_POOL)
    String pushNoticeToUser(Long memeberId, String templateId, String toPage, Map<String, Map<String, String>> mapData);

    /**
     * 微信通知服务
     * @param openid
     * @param templateId
     * @param mapData
     * @return
     */
    @Async(FebsConstant.ASYNC_POOL)
    String pushNoticeToUser(String openid, String templateId, String toPage, Map<String, Map<String, String>> mapData);
}
