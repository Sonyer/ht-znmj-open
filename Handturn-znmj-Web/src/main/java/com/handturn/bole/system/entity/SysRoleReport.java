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
 * 系统-角色报表 Entity
 *
 * @author Eric
 * @date 2020-02-16 20:13:20
 */
@Data
@TableName("sys_role_report")
@Excel("系统-角色报表")
public class SysRoleReport extends BasicEntity{

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
     * 报表id
     */
    @TableField("report_id")
    @ExcelField(value = "报表id")
    private Long reportId;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
