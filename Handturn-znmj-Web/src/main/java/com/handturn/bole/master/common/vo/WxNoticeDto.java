package com.handturn.bole.master.common.vo;

import lombok.Data;

/**
 * 中间传输类
 */
@Data
public class WxNoticeDto {
    String templateId;
    Object messageDataDto;

    String openId;
    String accessToken;
}
