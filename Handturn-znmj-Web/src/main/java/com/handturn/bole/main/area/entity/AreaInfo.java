package com.handturn.bole.main.area.entity;

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
 * 基础资料-区域管理 Entity
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
@Data
@TableName("area_info")
@Excel("基础资料-区域管理")
public class AreaInfo extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统编码
     */
    @TableField("area_code")
    @ExcelField(value = "系统编码")
    private String areaCode;

    /**
     * 定制编码
     */
    @TableField("area_client_code")
    @ExcelField(value = "定制编码")
    private String areaClientCode;


    /**
     * 区域名称
     */
    @TableField("area_name")
    @ExcelField(value = "区域名称")
    private String areaName;

    /**
     * 所属区域ID
     */
    @TableField("parent_area_id")
    @ExcelField(value = "所属区域ID")
    private Long parentAreaId;

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
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;


    @TableField(exist = false)
    private String parentAreaCode;
    @TableField(exist = false)
    private String parentAreaClientCode;
    @TableField(exist = false)
    private String parentAreaName;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
