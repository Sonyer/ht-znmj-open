package com.handturn.bole.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Eric
 */
@Data
@TableName("sys_log")
@Excel("系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户
     */
    @TableField("user_code")
    @ExcelField(value = "操作用户")
    private String userCode;

    /**
     * 操作用户
     */
    @TableField("user_name")
    @ExcelField(value = "操作用户")
    private String userName;

    /**
     * 操作内容
     */
    @TableField("operation")
    @ExcelField(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    @TableField("time")
    @ExcelField(value = "耗时")
    private Long time;
    /**
     * 操作方法
     */
    @TableField("method")
    @ExcelField(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    @TableField("params")
    @ExcelField(value = "方法参数")
    private String params;

    /**
     * 操作者ip
     */
    @TableField("ip")
    @ExcelField(value = "操作者ip")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 操作地点
     */
    @TableField("location")
    @ExcelField(value = "操作地点")
    private String location;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
