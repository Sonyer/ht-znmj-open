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
 * 系统-网点用户(数据权限) Entity
 *
 * @author MrBird
 * @date 2019-12-13 15:13:14
 */
@Data
@TableName("sys_oc_user")
@Excel("系统-网点用户(数据权限)")
public class SysOcUser extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ExcelField(value = "用户id")
    private Long userId;

    /**
     * 系统组织id
     */
    @TableField("org_id")
    @ExcelField(value = "系统组织id")
    private Long orgId;

    /**
     * 网点Id
     */
    @TableField("oc_id")
    @ExcelField(value = "网点Id")
    private Long ocId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
