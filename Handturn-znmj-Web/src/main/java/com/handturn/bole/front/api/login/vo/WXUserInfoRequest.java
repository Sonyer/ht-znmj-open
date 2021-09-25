package com.handturn.bole.front.api.login.vo;

import lombok.Data;

/**
 * 微信授权信息请求
 */
@Data
public class WXUserInfoRequest {
    //微信昵称
    private String nickName;
    //性别
    private String gender;
    //语言
    private String language;
    //城市
    private String city;
    //省份
    private String province;
    //国家
    private String country;
    //图像地址
    private String avatarUrl;
}
