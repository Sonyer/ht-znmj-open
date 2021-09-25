package com.handturn.bole.front.api.login.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信登陆返回信息
 */
@Data
public class WXLoginResponseVo implements Serializable {

    /**
     * 是否已授权
     */
    private String isAuth;

    /**
     * 是否手机号码已授权
     */
    private String isAuthPhoneNumber;

    /**
     * 平台会员账号-自动生成
     */
    private String accountCode;

    /**
     * 平台会员昵称
     */
    private String nickName;

    /**
     * 头像-访问地址
     */
    private String avatarRequest;

    /**
     * 认证类型:NORMAL_USER-普通用户,PERSION_USER-个人认证,CLIENT_USER-商户认证
     */
    private String certificationType;

    /**
     * 最后登陆经度
     */
    private BigDecimal lastLongitude;

    /**
     * 最后登陆纬度
     */
    private BigDecimal lastLatitude;

    /**
     * 用户验证的加密串
     */
    private String token;

}
