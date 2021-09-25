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
 * 系统-公司部门 Entity
 *
 * @author Eric
 * @date 2019-12-01 10:22:59
 */
@Data
@TableName("sys_organization_dep")
@Excel("系统-公司部门")
public class SysOrganizationDep extends BasicEntity {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门编号
     */
    @TableField("dep_code")
    @ExcelField(value = "部门编号")
    private String depCode;

    /**
     * 部门名称
     */
    @TableField("dep_name")
    @ExcelField(value = "部门名称")
    private String depName;

    /**
     * 部门简称
     */
    @TableField("dep_short_name")
    @ExcelField(value = "部门简称")
    private String depShortName;

    /**
     * 上级部门id
     */
    @TableField("parent_dep_id")
    private Long parentDepId;

    /**
     * 系统组织id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 所属网点
     */
    @TableField("oc_id")
    private Long ocId;

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
     * 父名称
     */
    @TableField(exist = false)
    @ExcelField(value = "父名称")
    private String parentDepName;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
