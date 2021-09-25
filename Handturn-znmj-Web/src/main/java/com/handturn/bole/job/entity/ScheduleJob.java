package com.handturn.bole.job.entity;

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
 * 定时任务表 Entity
 *
 * @author Eric
 * @date 2019-12-13 20:20:20
 */
@Data
@TableName("schedule_job")
@Excel("定时任务表")
public class ScheduleJob extends BasicEntity{

    /**
     * 任务调度参数 key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * cron表达式
     */
    @TableField("cron_expression")
    @ExcelField(value = "cron表达式")
    private String cronExpression;

    /**
     * 任务状态  NORMAL:正常  PAUSE:暂停
     */
    @TableField("status")
    @ExcelField(value = "任务状态  NORMAL:正常  PAUSE:暂停")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
