package com.handturn.bole.main.authArea.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.Data;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;

/**
 * 网点-权限区域 Entity
 *
 * @author Eric
 * @date 2020-09-22 08:57:47
 */
@Data
@TableName("oc_auth_area")
@Excel("网点-权限区域")
public class OcAuthArea extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区域编码
     */
    @TableField("area_code")
    @ExcelField(value = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @TableField("area_name")
    @ExcelField(value = "区域名称")
    private String areaName;

    /**
     * 网点编码
     */
    @TableField("oc_code")
    @ExcelField(value = "网点编码")
    private String ocCode;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ExcelField(value = "组织编码")
    private String orgCode;

    /**
     * 网点名称
     */
    @TableField("oc_name")
    @ExcelField(value = "网点名称")
    private String ocName;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ExcelField(value = "组织名称")
    private String orgName;

    /**
     * 状态: ENABLED-已启用;DISABLED-已禁用
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-已启用;DISABLED-已禁用")
    private String status;

    /**
     * 是否自动审核
     */
    @TableField("is_auto_audit")
    @ExcelField(value = "是否自动审核")
    private String isAutoAudit;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    //----------查询列表需要---------
    /**
     * 是否查询权限
     */
    @TableField(exist = false)
    private Boolean authCheck;
    /**
     * 查询访客ID
     */
    @TableField(exist = false)
    private Long visitorId;

    /**
     * 查询权限区域ID
     */
    @TableField(exist = false)
    private String layChecked;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
