package com.ht.znmj.websocket.bean.entity;

import lombok.Data;

import java.util.Date;

/**
 * 人员信息
 */
@Data
public class VisitorInfoAsyApplyReturnVo {
    private String name;
    private String phoneNumber;
    private String department;
    private String positor;
    private String idCard;
    private String wigan;
    private Date startTime;
    private Date endTime;
    private String cloudId;
}
