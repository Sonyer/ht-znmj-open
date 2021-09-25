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
 * 系统-角色 Entity
 *
 * @author MrBird
 * @date 2019-12-07 21:06:39
 */
@Data
@TableName("sys_role")
@Excel("系统-角色")
public class SysRole extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色编码
     */
    @TableField("role_code")
    @ExcelField(value = "角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @ExcelField(value = "角色名称")
    private String roleName;

    /**
     * 系统组织id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 所属营运组织id
     */
    @TableField("oc_id")
    private Long ocId;

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


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
