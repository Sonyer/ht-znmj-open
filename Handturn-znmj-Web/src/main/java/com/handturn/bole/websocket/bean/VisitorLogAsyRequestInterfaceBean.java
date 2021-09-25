package com.handturn.bole.websocket.bean;

import com.handturn.bole.websocket.bean.entity.VisitorLogVo;
import com.handturn.bole.websocket.constant.WebSocketConstant;
import lombok.Data;

/**
 * 心跳接口
 */
@Data
public class VisitorLogAsyRequestInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_VISITORLOG_REQUEST;
    private String faceFileStream;

    private VisitorLogVo visitorLog;
}
