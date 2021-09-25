package com.handturn.bole.front.api.home.vo;

import lombok.Data;

@Data
public class AuthAreaResponseVo {
    private String logoRequest;
    private String orgCode;
    private String orgName;
    private String ocCode;
    private String ocName;
    private String authAreaCode;
    private String authAreaName;
    private String remark;
    private String isAllowVisite;
    private String authStr;
}
