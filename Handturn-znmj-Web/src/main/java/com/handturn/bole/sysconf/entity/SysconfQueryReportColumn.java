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
 * 系统-报表查询列 Entity
 *
 * @author Eric
 * @date 2020-02-12 09:14:32
 */
@Data
@TableName("sysconf_query_report_column")
@Excel("系统-报表查询列")
public class SysconfQueryReportColumn extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报表模型id
     */
    @TableField("report_id")
    @ExcelField(value = "报表模型id")
    private Long reportId;

    /**
     * 报表编码
     */
    @TableField("report_code")
    @ExcelField(value = "报表编码")
    private String reportCode;

    /**
     * 列顺序号（用于sql排序）
     */
    @TableField("column_seq_num")
    @ExcelField(value = "列顺序号（用于sql排序）")
    private Integer columnSeqNum;

    /**
     * 参数编码
     */
    @TableField("column_code")
    @ExcelField(value = "参数编码")
    private String columnCode;

    /**
     * 参数名称
     */
    @TableField("column_name")
    @ExcelField(value = "参数名称")
    private String columnName;

    /**
     * 参数类型
     */
    @TableField("column_type")
    @ExcelField(value = "参数类型")
    private String columnType;

    /**
     * 参数初始化值
     */
    @TableField("column_init_value")
    @ExcelField(value = "参数初始化值")
    private String columnInitValue;

    /**
     * 参数默认值
     */
    @TableField("column_default_value")
    @ExcelField(value = "参数默认值")
    private String columnDefaultValue;

    /**
     * 列宽
     */
    @TableField("column_width")
    @ExcelField(value = "列宽")
    private String columnWidth;

    /**
     * 是否作为查询条件(1:是 0:否)
     */
    @TableField("is_query")
    @ExcelField(value = "是否作为查询条件(1:是 0:否)")
    private String isQuery;

    /**
     * 是否必填(1:是 0:否)
     */
    @TableField("is_require")
    @ExcelField(value = "是否必填(1:是 0:否)")
    private String isRequire;

    /**
     * 是否隐藏(1:是 0:否)
     */
    @TableField("is_hidden")
    @ExcelField(value = "是否隐藏(1:是 0:否)")
    private String isHidden;

    /**
     * 是否汇总字段(1:是 0:否)
     */
    @TableField("is_total_row_field")
    @ExcelField(value = "是否汇总字段(1:是 0:否)")
    private String isTotalRowField;

    /**
     * 格式化
     */
    @TableField("format_str")
    @ExcelField(value = "格式化")
    private String formatStr;

    /**
     * 时间间隔天数
     */
    @TableField("step")
    @ExcelField(value = "时间间隔天数")
    private Long step;

    /**
     * 条件查询
     */
    @TableField("query_method")
    @ExcelField(value = "条件查询方式")
    private String queryMethod;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
