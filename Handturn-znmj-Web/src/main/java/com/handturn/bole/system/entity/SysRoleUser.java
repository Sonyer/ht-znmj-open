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
 * 系统-角色用户 Entity
 *
 * @author MrBird
 * @date 2019-12-13 15:14:41
 */
@Data
@TableName("sys_role_user")
@Excel("系统-角色用户")
public class SysRoleUser extends BasicEntity{

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
     * 用户id
     */
    @TableField("user_id")
    @ExcelField(value = "用户id")
    private Long userId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
