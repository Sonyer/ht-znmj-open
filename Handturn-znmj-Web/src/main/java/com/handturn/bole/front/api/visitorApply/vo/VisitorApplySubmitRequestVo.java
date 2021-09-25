package com.handturn.bole.front.api.visitorApply.vo;

import lombok.Data;

/**
 * 访客提交
 */
@Data
public class VisitorApplySubmitRequestVo {
    private String orgCode;
    private String ocCode;
    private String authAreaCode;

    private String id;
    private String idCardName;
    private String idCard;
    private String phoneNumber;
    private String faceImgRequest;
    private String positionName;
    private String remark;

    private String validateCode;
}
