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
 * 系统-角色网点 Entity
 *
 * @author MrBird
 * @date 2019-12-13 15:07:51
 */
@Data
@TableName("sys_role_oc")
@Excel("系统-角色网点")
public class SysRoleOc extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ExcelField(value = "角色id")
    private Long roleId;

    /**
     * 网点id
     */
    @TableField("oc_id")
    @ExcelField(value = "网点id")
    private Long ocId;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
