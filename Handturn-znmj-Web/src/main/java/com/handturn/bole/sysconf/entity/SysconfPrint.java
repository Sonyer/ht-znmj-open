package com.handturn.bole.sysconf.entity;

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
 * 系统配置-打印配置 Entity
 *
 * @author Eric
 * @date 2020-01-31 09:15:26
 */
@Data
@TableName("sysconf_print")
@Excel("系统配置-打印配置")
public class SysconfPrint extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 打印编码
     */
    @TableField("print_code")
    @ExcelField(value = "打印编码")
    private String printCode;

    /**
     * 打印名称
     */
    @TableField("print_name")
    @ExcelField(value = "打印名称")
    private String printName;

    /**
     * 打印分组
     */
    @TableField("group_name")
    @ExcelField(value = "打印分组")
    private String groupName;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 是否系统创建(1:是 0:否)，系统创建的记录不能前台修改和删除
     */
    @TableField("is_sys_created")
    @ExcelField(value = "是否系统创建(1:是 0:否)，系统创建的记录不能前台修改和删除")
    private String isSysCreated;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
