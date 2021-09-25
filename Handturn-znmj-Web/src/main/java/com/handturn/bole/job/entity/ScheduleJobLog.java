package com.handturn.bole.job.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * 调度日志表 Entity
 *
 * @author Eric
 * @date 2019-12-13 20:20:24
 */
@Data
@TableName("schedule_job_log")
@Excel("调度日志表")
public class ScheduleJobLog {

    /**
     * 任务日志id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务id
     */
    @TableField("job_id")
    @ExcelField(value = "任务id")
    private Long jobId;

    /**
     * spring bean名称
     */
    @TableField("bean_name")
    @ExcelField(value = "spring bean名称")
    private String beanName;

    /**
     * 方法名
     */
    @TableField("method_name")
    @ExcelField(value = "方法名")
    private String methodName;

    /**
     * 参数
     */
    @TableField("params")
    @ExcelField(value = "参数")
    private String params;

    /**
     * 任务状态 SUCCESS:成功    ERROR:失败
     */
    @TableField("status")
    @ExcelField(value = "任务状态 SUCCESS:成功    ERROR:失败")
    private String status;

    /**
     * 失败信息
     */
    @TableField("error")
    @ExcelField(value = "失败信息")
    private String error;

    /**
     * 耗时(单位:毫秒)
     */
    @TableField("times")
    @ExcelField(value = "耗时(单位:毫秒)")
    private Long times;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_upd_date",fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdDate;
}
