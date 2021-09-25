package com.handturn.bole.websocket.bean.entity;

import lombok.Data;

/**
 * 连接的接口Bean
 */
@Data
public class WebSocketOpenBean {
    private String macId;
    private String lanIp;
    private String ocCode;
    private String appVersion;
    private String systemName;
    private String systemVersion;
}
