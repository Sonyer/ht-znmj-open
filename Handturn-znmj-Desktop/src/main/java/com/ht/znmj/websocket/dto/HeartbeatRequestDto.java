package com.ht.znmj.websocket.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 心跳返回
 */
@Data
public class HeartbeatRequestDto {
    private Date startDateTime;
    private Date endDaateTime;
    private Integer maxLogSeq;
    private Set<String> equipmentMacIds;
}
