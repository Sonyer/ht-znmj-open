package com.ht.znmj.websocket.bean;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.List;

/**
 * 心跳接口
 */
@Data
public class EquipmentAsyApplyReturnInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_ASY_APPLY_EQUIPMENT_RETURN;

    private List<EquipmentInfo> equipmentInfoList;
}
