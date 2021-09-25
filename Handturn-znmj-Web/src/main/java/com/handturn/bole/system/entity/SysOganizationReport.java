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
 * 系统-组织报表 Entity
 *
 * @author Eric
 * @date 2020-02-16 20:08:44
 */
@Data
@TableName("sys_oganization_report")
@Excel("系统-组织报表")
public class SysOganizationReport extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织id
     */
    @TableField("org_id")
    @ExcelField(value = "组织id")
    private Long orgId;

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
