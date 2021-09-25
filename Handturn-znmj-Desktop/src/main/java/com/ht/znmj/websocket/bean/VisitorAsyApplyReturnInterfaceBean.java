package com.ht.znmj.websocket.bean;

import com.ht.znmj.websocket.bean.entity.VisitorInfoAsyApplyReturnVo;
import com.ht.znmj.websocket.constant.WebSocketConstant;
import lombok.Data;

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
