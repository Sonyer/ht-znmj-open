package com.handturn.bole.websocket.bean;

import com.handturn.bole.websocket.bean.entity.VisitorInfoAsyApplyReturnVo;
import com.handturn.bole.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 心跳接口
 */
@Data
public class VisitorAsyApplyReturnInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_ASY_APPLY_VISITOR_RETURN;

    private String authType;

    private String faceFileStream;
    private Set<String> equipmentMacs;

    private VisitorInfoAsyApplyReturnVo visitorInfoAsyApplyReturnVo;
}
