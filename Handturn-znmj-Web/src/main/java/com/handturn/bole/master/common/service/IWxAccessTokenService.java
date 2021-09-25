package com.handturn.bole.master.common.service;

import com.handturn.bole.master.set.entity.MinichatSet;

/**
 * 微信通知服务
 */
public interface IWxAccessTokenService {

    /**
     * 获取微信小程序AccessTocken
     * @return
     */
    String getAccessToken4Wx();

    /**
     * 获取微信小程序AccessTocken
     * @return
     */
    String getAccessToken4Wx(MinichatSet minichatSet);
}
