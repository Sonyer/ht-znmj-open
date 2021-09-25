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

/**
 * 系统-组织 Entity
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
@Data
@TableName("sys_organization")
@Excel("系统-组织")
public class SysOrganization extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织编号
     */
    @TableField("org_code")
    @ExcelField(value = "组织编号")
    private String orgCode;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ExcelField(value = "组织名称")
    private String orgName;

    /**
     * 组织简称
     */
    @TableField("org_short_name")
    @ExcelField(value = "组织简称")
    private String orgShortName;

    /**
     * 地址
     */
    @TableField("address")
    @ExcelField(value = "地址")
    private String address;

    /**
     * 区域
     */
    @TableField("region")
    @ExcelField(value = "区县")
    private String region;

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
     * 邮编
     */
    @TableField("post_code")
    @ExcelField(value = "邮编")
    private String postCode;

    /**
     * 电话
     */
    @TableField("tel")
    @ExcelField(value = "电话")
    private String tel;

    /**
     * 手机
     */
    @TableField("cell")
    @ExcelField(value = "手机")
    private String cell;

    /**
     * 传真
     */
    @TableField("fax")
    @ExcelField(value = "传真")
    private String fax;

    /**
     * 邮箱
     */
    @TableField("email")
    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 联系人
     */
    @TableField("attention_to")
    @ExcelField(value = "联系人")
    private String attentionTo;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 组织类别(SYS-系统组织,ORG-客户组织)
     */
    @TableField("org_type")
    @ExcelField(value = "组织类别(SYS-系统组织,ORG-客户组织)")
    private String orgType;

    /**
     * logo文件名
     */
    @TableField("logo_file_name")
    @ExcelField(value = "logo文件名")
    private String logoFileName;

    /**
     * Logo文件路径
     */
    @TableField("logo_file_path")
    @ExcelField(value = "Logo文件路径")
    private String logoFilePath;

    /**
     * Logo文件访问路径
     */
    @TableField("logo_request_url")
    @ExcelField(value = "Logo文件访问路径")
    private String logoRequestUrl;


    /**
     * 关联的初始化系统
     */
    @TableField("relate_oc_id")
    private Long relateOcId;

    /**
     * 关联的初始化系统
     */
    @TableField("relate_role_id")
    private Long relateRoleId;

    /**
     * 关联的初始化系统
     */
    @TableField("relate_user_id")
    private Long relateUserId;

    /**
     * 关联的初始化系统
     */
    @TableField("relate_dep_id")
    private Long relateDepId;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
