package com.ht.znmj.websocket.service;

import com.ht.znmj.entity.CloudInterfaceInfo;

public interface IWebSocketClientService {
    /**
     * 群发
     * @param message
     */
    void groupSending(String message);

    /**
     * 指定发送
     * @param name
     * @param message
     */
    void appointSending(String name,String message);


    /**
     * 初始化连接sokect
     * @param
     * @return
     */
    Boolean initWebSocketClient();


    /**
     * 连接sokect
     * @param cloudInterfaceInfo
     * @return
     */
    Boolean connectWebSocketServer(CloudInterfaceInfo cloudInterfaceInfo);


    /**
     * 关闭连接sokect
     * @param cloudInterfaceInfo
     * @return
     */
    Boolean closeWebSocketServer(CloudInterfaceInfo cloudInterfaceInfo);

    /**
     * 发送同步请求
     * @return
     */
    Boolean sendAsyApply();

}
