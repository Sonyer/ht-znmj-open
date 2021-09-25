package com.handturn.bole.front.api.visitorApply.vo;

import lombok.Data;

@Data
public class VisitorApplyResponseVo {
    private String id;
    private String idCardName;
    private String idCard;
    private String phoneNumber;
    private String faceImgRequest;
    private String positionName;
    private String auditStatus;
    private String remark;
}
