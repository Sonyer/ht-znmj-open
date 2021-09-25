package com.handturn.bole.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统-用户 Entity
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
@Data
@TableName("sys_user")
@Excel("系统-用户")
public class SysUser extends BasicEntity implements Serializable{

    // 用户状态:有效
    public static final String STATUS_VALID = "1";
    // 用户状态:锁定
    public static final String STATUS_LOCK = "0";
    // 默认头像
    public static final String DEFAULT_AVATAR = "default.jpg";
    // 默认密码
    public static final String DEFAULT_PASSWORD = "123456";
    // 性别男
    public static final String SEX_MALE = "0";
    // 性别女
    public static final String SEX_FEMALE = "1";
    // 性别保密
    public static final String SEX_UNKNOW = "2";
    // 黑色主题
    public static final String THEME_BLACK = "black";
    // 白色主题
    public static final String THEME_WHITE = "white";
    // TAB开启
    public static final String TAB_OPEN = "1";
    // TAB关闭
    public static final String TAB_CLOSE = "0";

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编码
     */
    @TableField("user_code")
    @ExcelField(value = "用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @TableField("user_name")
    @ExcelField(value = "用户名称")
    private String userName;

    /**
     * 系统组织id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 所属部门id
     */
    @TableField("dep_id")
    private Long depId;

    /**
     * 所属营运中心id
     */
    @TableField("oc_id")
    private Long ocId;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 描述
     */
    @TableField("description")
    @ExcelField(value = "描述")
    private String description;

    /**
     * 住址
     */
    @TableField("address")
    @ExcelField(value = "住址")
    private String address;

    /**
     * 邮箱
     */
    @TableField("email")
    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 手机
     */
    @TableField("mobile")
    @ExcelField(value = "手机")
    private String mobile;

    /**
     * 电话
     */
    @TableField("phone")
    @ExcelField(value = "电话")
    private String phone;

    /**
     * 登陆时间
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 是否系统创建 0-否，1-是
     */
    @TableField("is_sys_create")
    private String isSysCreate;

    /**
     * 性别(1:男,2:女)
     */
    @TableField("sex")
    @ExcelField(value = "性别(1:男,2:女)")
    private String sex;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 是否首次登陆 0不是 1是
     */
    @TableField("first_login")
    private Boolean firstLogin;


    /**
     * 是否开启Tab
     */
    @TableField("is_tab")
    private String isTab;

    /**
     * 管理口令代码
     */
    @TableField("command_code")
    private String commandCode;
    /**
     * 管理口令失效时间
     */
    @TableField("command_expires_time")
    private Date commandExpiresTime;

    /**
     * 是否已绑定前端账号
     */
    @TableField("is_bind_member")
    private String isBindMember;

    /**
     * 前端账号Id
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 前端账号编码
     */
    @TableField("member_account_code")
    private String memberAccountCode;

    /**
     * 部门名称
     */
    @ExcelField(value = "部门")
    @TableField(exist = false)
    private String depName;

    /**
     * 角色 ID
     */
    @TableField(exist = false)
    private Long roleId;

    @TableField(exist = false)
    private String roleName;

    /**
     * 用户所属组织
     */
    @TableField(exist = false)
    private SysOrganization org;
    @TableField(exist = false)
    private SysOrganizationOc oc;

    /**
     * 当前线程组织
     */
    @TableField(exist = false)
    private SysOrganization currentOrg;
    /**
     * 当前线程网点
     */
    @TableField(exist = false)
    private SysOrganizationOc currentOc;

    @TableField(exist = false)
    private SysResource currentRootResource;

    /**
     * 移动口令是否失效
     */
    @TableField(exist = false)
    private String commandCodeIsExpires;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
