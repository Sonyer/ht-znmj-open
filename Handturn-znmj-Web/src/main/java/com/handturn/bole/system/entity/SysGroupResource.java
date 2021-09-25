package com.handturn.bole.system.entity;

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
 * 系统-组资源 Entity
 *
 * @author Eric
 * @date 2020-05-23 14:55:50
 */
@Data
@TableName("sys_group_resource")
@Excel("系统-组资源")
public class SysGroupResource extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组id
     */
    @TableField("group_id")
    @ExcelField(value = "组id")
    private Long groupId;

    /**
     * 资源id
     */
    @TableField("resource_id")
    @ExcelField(value = "资源id")
    private Long resourceId;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
