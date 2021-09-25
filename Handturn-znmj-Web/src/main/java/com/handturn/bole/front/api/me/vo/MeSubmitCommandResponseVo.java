package com.handturn.bole.front.api.me.vo;

import lombok.Data;

/**
 * 初始化Me页面
 */
@Data
public class MeSubmitCommandResponseVo {

    private String commandCode;
    private String userName;
    private String phoneNumber;
    private String validateCode;
}
