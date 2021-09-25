package com.handturn.bole.front.api.me.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 初始化Me页面
 */
@Data
public class InitMeResponseVo {

    private String accountCode;
    private String nickName;
    private String gender;
    private String avatarRequest;
    private String phoneNumber;
    private String vipLevel;
    private String certificationType;
    private String isPersionUser;
    private String isClientUser;

    private String persionUserCerStatus;
    private String clientUserCerStatus;

    private String persionUserCerPayStatus;
    private String clientUserCerPayStatus;

    private String persionUserPayBusNo;
    private String clientUserPayBusNo;


    private BigDecimal viewCount;
    private BigDecimal admireCount;
    private BigDecimal commentCount;
    private BigDecimal shareCount;
    private Integer followCount;

    private String copyright;
    private String aboutUs;
    private String instDocRequest;

    private List<String> wxNotifyTemplatIds;
}
