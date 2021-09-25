package com.handturn.bole.main.authArea.entity;

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
 * 网点-访客区域权限 Entity
 *
 * @author Eric
 * @date 2020-09-22 08:58:06
 */
@Data
@TableName("oc_auth_area_visitor")
@Excel("网点-访客区域权限")
public class OcAuthAreaVisitor extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限区域
     */
    @TableField("auth_area_id")
    @ExcelField(value = "权限区域")
    private Long authAreaId;

    /**
     * 网点-访客ID
     */
    @TableField("oc_visitor_id")
    @ExcelField(value = "网点-访客ID")
    private Long ocVisitorId;

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
     * 有效开始时间
     */
    @TableField("effective_time_begin")
    @ExcelField(value = "有效开始时间")
    private Date effectiveTimeBegin;

    /**
     * 有效结束时间
     */
    @TableField("effective_time_end")
    @ExcelField(value = "有效结束时间")
    private Date effectiveTimeEnd;

    /**
     * 状态: ENABLED-已启用;DISABLED-已禁用
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-已启用;DISABLED-已禁用")
    private String status;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
