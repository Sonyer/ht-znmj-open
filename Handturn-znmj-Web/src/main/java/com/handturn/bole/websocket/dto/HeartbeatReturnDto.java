package com.handturn.bole.websocket.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 心跳返回
 */
@Data
public class HeartbeatReturnDto {
    private Date startDateTime;
    private Date endDaateTime;
    private String ocCode;
    private Set<String> equipmentMacIds;
}
