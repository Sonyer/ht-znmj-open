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
 * 网点-访客区域责任人 Entity
 *
 * @author Eric
 * @date 2020-09-30 08:32:35
 */
@Data
@TableName("oc_auth_area_responsible")
@Excel("网点-访客区域责任人")
public class OcAuthAreaResponsible extends BasicEntity{

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
     * 负责人用户ID
     */
    @TableField("user_id")
    @ExcelField(value = "负责人用户ID")
    private Long userId;

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


    @TableField(exist = false)
    private String userCode;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String depName;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
