package com.ht.znmj.websocket.bean.entity;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.List;

/**
 * 心跳接口
 */
@Data
public class HeartbeatInterfaceBean extends BasicInterfaceBean{

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_HEARTBEAT;

    private List<EquipmentInfo> equipmentInfoList;
}
