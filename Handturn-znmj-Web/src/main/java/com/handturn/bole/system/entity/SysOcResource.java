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
 * 系统-网点资源 Entity
 *
 * @author MrBird
 * @date 2019-12-13 10:00:36
 */
@Data
@TableName("sys_oc_resource")
@Excel("系统-网点资源")
public class SysOcResource extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 网点id
     */
    @TableField("oc_id")
    @ExcelField(value = "网点id")
    private Long ocId;

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
