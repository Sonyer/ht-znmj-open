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
 * 智能门禁-平台会员 Entity
 *
 * @author Eric
 * @date 2020-03-13 20:42:35
 */
@Data
@TableName("member")
@Excel("智能门禁-平台会员")
public class Member implements Serializable {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台会员账号-自动生成
     */
    @TableField("account_code")
    @ExcelField(value = "平台会员账号-自动生成")
    private String accountCode;

    /**
     * 平台会员昵称
     */
    @TableField("nick_name")
    @ExcelField(value = "平台会员昵称")
    private String nickName;

    /**
     * 性别
     */
    @TableField("gender")
    @ExcelField(value = "性别")
    private String gender;

    /**
     * 头像-存根地址
     */
    @TableField("avatar_attchment")
    @ExcelField(value = "头像-存根地址")
    private String avatarAttchment;

    /**
     * 头像-访问地址
     */
    @TableField("avatar_request")
    @ExcelField(value = "头像-访问地址")
    private String avatarRequest;

    /**
     * 用户手机号
     */
    @TableField("phone_number")
    @ExcelField(value = "用户手机号")
    private String phoneNumber;

    /**
     * 用户邮箱
     */
    @TableField("email")
    @ExcelField(value = "用户邮箱")
    private String email;

    /**
     * 会员等级
     */
    @TableField("vip_level")
    @ExcelField(value = "会员等级")
    private String vipLevel;

    /**
     * 认证类型:NORMAL_USER-普通用户,PERSION_USER-个人认证,CLIENT_USER-商户认证
     */
    @TableField("certification_type")
    @ExcelField(value = "认证类型:NORMAL_USER-普通用户,PERSION_USER-个人认证,CLIENT_USER-商户认证")
    private String certificationType;

    /**
     * 个人已认证
     */
    @TableField("is_persion_user")
    @ExcelField(value = "个人已认证")
    private String isPersionUser;

    /**
     * 商户已认证
     */
    @TableField("is_client_user")
    @ExcelField(value = "商户已认证")
    private String isClientUser;

    /**
     * 用户状态:NOMAL-正常,FREEZE-冻结
     */
    @TableField("status")
    @ExcelField(value = "用户状态:NORMAL-正常,FREEZE-冻结")
    private String status;

    /**
     * 特约会员
     */
    @TableField("special_user")
    @ExcelField(value = "特约会员")
    private String specialUser;

    /**
     * 特约时间
     */
    @TableField("special_user_time")
    @ExcelField(value = "特约时间")
    private Date specialUserTime;

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

    /**
     * 关注数
     */
    @TableField("follow_count")
    @ExcelField(value = "关注数")
    private Integer followCount;
}
