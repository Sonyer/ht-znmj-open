package com.handturn.bole.master.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 智能门禁-微信小程序会员 Entity
 *
 * @author Eric
 * @date 2020-03-16 13:19:19
 */
@Data
@TableName("member_wx_mp")
@Excel("智能门禁-微信小程序会员")
public class MemberWxMp implements Serializable {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小程序openid
     */
    @TableField("openid")
    @ExcelField(value = "小程序openid")
    private String openid;

    /**
     * 微信用户唯一ID
     */
    @TableField("unionid")
    @ExcelField(value = "微信用户唯一ID")
    private String unionid;

    /**
     * 绑定平台用户ID
     */
    @TableField("bind_member_id")
    @ExcelField(value = "绑定平台用户ID")
    private Long bindMemberId;

    /**
     * 微信头像-访问地址
     */
    @TableField("avatar_request")
    @ExcelField(value = "微信头像-访问地址")
    private String avatarRequest;

    /**
     * 平台会员昵称
     */
    @TableField("nick_name")
    @ExcelField(value = "平台会员昵称")
    private String nickName;

    /**
     * 城市
     */
    @TableField("city")
    @ExcelField(value = "城市")
    private String city;

    /**
     * 省份
     */
    @TableField("province")
    @ExcelField(value = "省份")
    private String province;

    /**
     * 国家
     */
    @TableField("country")
    @ExcelField(value = "国家")
    private String country;

    /**
     * 语言
     */
    @TableField("language")
    @ExcelField(value = "语言")
    private String language;

    /**
     * 性别
     */
    @TableField("gender")
    @ExcelField(value = "性别")
    private String gender;

    /**
     * 手机号
     */
    @TableField("phone_number")
    @ExcelField(value = "手机号")
    private String phoneNumber;

    /**
     * 钱包手机号
     */
    @TableField("pur_phone_number")
    @ExcelField(value = "钱包手机号")
    private String purPhoneNumber;

    /**
     * 国家编码
     */
    @TableField("country_code")
    @ExcelField(value = "国家编码")
    private String countryCode;

    /**
     * 新增时间
     */
    @TableField("create_time")
    @ExcelField(value = "新增时间")
    private Date createTime;

    /**
     * 是否授权
     */
    @TableField("is_auth")
    @ExcelField(value = "是否授权")
    private String isAuth;

    /**
     * 用户状态:NOMAL-正常,FREEZE-冻结
     */
    @TableField("status")
    @ExcelField(value = "用户状态:NOMAL-正常,FREEZE-冻结")
    private String status;

    /**
     * 登陆次数
     */
    @TableField("login_count")
    @ExcelField(value = "登陆次数")
    private Integer loginCount;

    /**
     * 最后登陆IP
     */
    @TableField("last_login_ip")
    @ExcelField(value = "最后登陆IP")
    private String lastLoginIp;

    /**
     * 最后登陆IP区域
     */
    @TableField("last_ip_region")
    @ExcelField(value = "最后登陆IP区域")
    private String lastIpRegion;

    /**
     * 第一次登陆时间
     */
    @TableField("first_login_time")
    @ExcelField(value = "第一次登陆时间")
    private Date firstLoginTime;

    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    @ExcelField(value = "最后登陆时间")
    private Date lastLoginTime;

    /**
     * 最后登陆经度
     */
    @TableField("last_longitude")
    @ExcelField(value = "最后登陆经度")
    private BigDecimal lastLongitude;

    /**
     * 最后登陆纬度
     */
    @TableField("last_latitude")
    @ExcelField(value = "最后登陆纬度")
    private BigDecimal lastLatitude;


}
