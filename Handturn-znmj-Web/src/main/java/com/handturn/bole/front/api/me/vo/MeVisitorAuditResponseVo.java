package com.handturn.bole.front.api.me.vo;

import lombok.Data;

import java.util.List;


/**
 * 初始化Me访客审核页面
 */
@Data
public class MeVisitorAuditResponseVo {

    private List<InitMeOrderDataResponseVo> pageDatas;

    private List<String> wxNotifyTemplatIds;

    @Data
    public static class InitMeOrderDataResponseVo{
        private String id;
        private String idCardName;
        private String idCard;
        private String ocCode;
        private String ocName;
        private String authAreaCode;
        private String authAreaName;
        private String phoneNumber;
        private String faceImgRequest;
        private String positionName;
        private String auditStatus;
        private String remark;
    }
}
