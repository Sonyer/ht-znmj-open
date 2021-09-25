package com.handturn.bole.websocket.bean;

import com.handturn.bole.websocket.bean.entity.EquipmentInfoVo;
import com.handturn.bole.websocket.constant.WebSocketConstant;
import lombok.Data;

import java.util.List;

/**
 * 心跳接口
 */
@Data
public class EquipmentAsyApplyReturnInterfaceBean {

    private String interfaceType = WebSocketConstant.INTERFACE_TYPE_ASY_APPLY_EQUIPMENT_RETURN;

    private List<EquipmentInfoVo> equipmentInfoList;
}
