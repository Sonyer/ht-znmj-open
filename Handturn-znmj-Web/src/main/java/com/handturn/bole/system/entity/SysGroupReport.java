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
 * 系统-组报表 Entity
 *
 * @author Eric
 * @date 2020-05-23 14:55:39
 */
@Data
@TableName("sys_group_report")
@Excel("系统-组报表")
public class SysGroupReport extends BasicEntity{

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
