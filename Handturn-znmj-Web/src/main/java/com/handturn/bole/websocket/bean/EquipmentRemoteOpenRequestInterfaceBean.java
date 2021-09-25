package com.handturn.bole.websocket.bean;

import com.handturn.bole.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.Set;

/**
 * 心跳接口
 */
@Data
public class EquipmentRemoteOpenRequestInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_EQUIPMENT_REMOTEOPEN;

    private Set<String> equipmentSn;
}
