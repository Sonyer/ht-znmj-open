package com.handturn.bole.websocket.bean;

import com.handturn.bole.websocket.bean.entity.EquipmentInfoVo;
import com.handturn.bole.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.List;

/**
 * 心跳接口
 */
@Data
public class HeartbeatInterfaceBean{

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_HEARTBEAT;

    private List<EquipmentInfoVo> equipmentInfoList;
}
