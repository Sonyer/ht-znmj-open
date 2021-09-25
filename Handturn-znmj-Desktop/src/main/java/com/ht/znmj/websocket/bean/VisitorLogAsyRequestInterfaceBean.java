package com.ht.znmj.websocket.bean;

import com.ht.znmj.entity.VisitorLog;
import com.ht.znmj.websocket.constant.WebSocketConstant;
import lombok.Data;

/**
 * 心跳接口
 */
@Data
public class VisitorLogAsyRequestInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_VISITORLOG_REQUEST;
    private String faceFileStream;

    private VisitorLog visitorLog;
}
