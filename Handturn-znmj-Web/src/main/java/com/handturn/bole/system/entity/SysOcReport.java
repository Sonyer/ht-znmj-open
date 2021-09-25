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
 * 系统-网点报表 Entity
 *
 * @author Eric
 * @date 2020-02-16 20:11:11
 */
@Data
@TableName("sys_oc_report")
@Excel("系统-网点报表")
public class SysOcReport extends BasicEntity{

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
