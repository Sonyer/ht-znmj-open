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
 * 网点-访客区域权限日志 Entity
 *
 * @author Eric
 * @date 2020-12-05 20:04:29
 */
@Data
@TableName("oc_auth_area_log")
@Excel("网点-访客区域权限日志")
public class OcAuthAreaLog extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    @TableField("equipment_id")
    @ExcelField(value = "设备ID")
    private Long equipmentId;

    /**
     * 设备MAC
     */
    @TableField("equipment_mac")
    @ExcelField(value = "设备MAC")
    private String equipmentMac;

    /**
     * 网点访客ID
     */
    @TableField("oc_visitor_id")
    @ExcelField(value = "网点访客ID")
    private Long ocVisitorId;

    /**
     * 授权类型:0-取消授权;1-授权;
     */
    @TableField("auth_type")
    @ExcelField(value = "授权类型:0-取消授权;1-授权;")
    private String authType;

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


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
