package com.handturn.bole.front.api.me.vo;

import lombok.Data;


/**
 * 初始化Me通知页面
 */
@Data
public class InitMeNoticeSetResponseVo {

    private String notifyType;
    private String wxNotifyTemplateId;
    private String ownerType;
    private String notifyDec;
    private Boolean status = false;

}
