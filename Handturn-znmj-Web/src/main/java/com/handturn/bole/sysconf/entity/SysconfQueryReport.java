package com.handturn.bole.sysconf.entity;

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
 * 系统-报表 Entity
 *
 * @author Eric
 * @date 2020-02-12 09:06:44
 */
@Data
@TableName("sysconf_query_report")
@Excel("系统-报表")
public class SysconfQueryReport extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报表编码
     */
    @TableField("report_code")
    @ExcelField(value = "报表编码")
    private String reportCode;

    /**
     * 报表名称
     */
    @TableField("report_name")
    @ExcelField(value = "报表名称")
    private String reportName;

    /**
     * 报表英文名
     */
    @TableField("report_name_en")
    @ExcelField(value = "报表英文名")
    private String reportNameEn;

    /**
     * //报表类型:REPORT_ROOT-根目录;REPORT_MENU-目录;REPORT-报表
     */
    @TableField("report_node_type")
    @ExcelField(value = "//报表类型:REPORT_ROOT-根目录;REPORT_MENU-目录;REPORT-报表")
    private String reportNodeType;

    /**
     * 排序
     */
    @TableField("sort_no")
    @ExcelField(value = "排序")
    private Integer sortNo;

    /**
     * 父节点id
     */
    @TableField("parent_report_id")
    @ExcelField(value = "父节点id")
    private Long parentReportId;

    /**
     * 根节点id
     */
    @TableField("root_report_id")
    @ExcelField(value = "根节点id")
    private Long rootReportId;

    /**
     * 报表模板数据源
     */
    @TableField("data_source")
    @ExcelField(value = "报表模板数据源")
    private String dataSource;

    /**
     * sql脚本
     */
    @TableField("sql_script")
    @ExcelField(value = "sql脚本")
    private String sqlScript;

    /**
     * 排序sql
     */
    @TableField("sql_sort_script")
    @ExcelField(value = "排序sql")
    private String sqlSortScript;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 描述
     */
    @TableField("description")
    @ExcelField(value = "描述")
    private String description;

    /**
     * 父资源名称
     */
    @TableField(exist = false)
    @ExcelField(value = "父资源名称")
    private String parentReportName;

    /**
     * 主模块名
     */
    @TableField(exist = false)
    @ExcelField(value = "主模块名称")
    private String rootReportName;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
