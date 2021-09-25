package com.handturn.bole.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统-角色资源 Entity
 *
 * @author Eric
 * @date 2019-12-01 10:39:10
 */
@Data
@TableName("sys_role_resource")
public class SysRoleResource extends BasicEntity {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private Long resourceId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
