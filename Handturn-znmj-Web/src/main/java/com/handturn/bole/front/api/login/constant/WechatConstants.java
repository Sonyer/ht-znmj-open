package com.handturn.bole.front.api.login.constant;

/**
 * 微信配置变量
 */
public class WechatConstants {
    //请求
    public static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";  //微信授权请求地址
    public static final String APPID = "appid";       //开发者设置中的appId
    public static final String SECRET = "secret";     //开发者设置中的appSecret
    public static final String JS_CODE = "js_code";    //小程序调用wx.login返回的code
    public static final String GRANT_TYPE = "grant_type";    //授权类型
    public static final String GRANT_TYPE_authorization_code = "authorization_code";    //默认授权类型

    //返回
    public static final String OPENID = "openid";    //默认授权类型
    public static final String SESSION_KEY = "session_key";    //默认授权类型
    public static final String UNIONID = "unionid";    //默认授权类型
    public static final String NICK_NAME = "nickName";
    public static final String LANGUAGE = "language";
    public static final String GENDER = "gender";
    public static final String CITY = "city";
    public static final String PROVINCE = "province";
    public static final String COUNTRY = "country";
    public static final String AVATAR_URL = "avatarUrl";

    //手机号授权返回
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String PUR_PHONE_NUMBER = "purPhoneNumber";
    public static final String COUNTRY_CODE = "countryCode";
}
