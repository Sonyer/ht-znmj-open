package com.handturn.bole.master.common.vo;

import lombok.Data;

import java.util.Map;

/**
 *微信通知 返回消息
 */
@Data
public class WxNoticeRequestVo {
    private String touser;//用户openid
    private String template_id;//模版id
    private String page = "pages/index/index";//默认跳到小程序首页
    private String miniprogram_state = "";
    private Map<String, Map<String,String>> data;//推送文字
}
