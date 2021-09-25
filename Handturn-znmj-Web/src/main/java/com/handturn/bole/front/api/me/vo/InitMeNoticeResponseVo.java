package com.handturn.bole.front.api.me.vo;

import lombok.Data;

import java.util.Date;


/**
 * 初始化Me通知页面
 */
@Data
public class InitMeNoticeResponseVo {

    private String notifyType;
    private String notifyMessage;
    private String page;
    private String readStatus;
    private Date createDate;
}
