package com.handturn.bole.monitor.service;

import com.handturn.bole.monitor.entity.ActiveUser;

import java.util.List;

/**
 * @author Eric
 */
public interface ISessionService {

    /**
     * 获取在线用户列表
     *
     * @param username 用户名
     * @return List<ActiveUser>
     */
    List<ActiveUser> list(String username);

    /**
     * 踢出用户
     *
     * @param sessionId sessionId
     */
    void forceLogout(String sessionId);
}
