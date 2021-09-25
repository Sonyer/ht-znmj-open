package com.ht.znmj.view.service;

import com.ht.znmj.entity.CloudInterfaceInfo;
import com.ht.znmj.websocket.service.IWebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 页面调用接口
 */
@Component
public class WebSocketUtil {
    @Autowired
    private IWebSocketClientService webSocketService;

    private static WebSocketUtil webSocketUtil;

    @PostConstruct
    public void init(){
        webSocketUtil = this;
        webSocketUtil.webSocketService = this.webSocketService;
    }

    public static Boolean initWebSocketClient(){
        return webSocketUtil.webSocketService.initWebSocketClient();
    }

    public static Boolean connectWebSocketServer(CloudInterfaceInfo cloudInterfaceInfo){
        return webSocketUtil.webSocketService.connectWebSocketServer(cloudInterfaceInfo);
    }
    public static Boolean closeWebSocketServer(CloudInterfaceInfo cloudInterfaceInfo){
        return webSocketUtil.webSocketService.closeWebSocketServer(cloudInterfaceInfo);
    }

    public static Boolean sendAsyApply(){
        return webSocketUtil.webSocketService.sendAsyApply();
    }
}
