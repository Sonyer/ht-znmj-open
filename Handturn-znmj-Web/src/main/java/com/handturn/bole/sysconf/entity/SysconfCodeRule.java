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
 * 系统配置-编码规则 Entity
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
@Data
@TableName("sysconf_code_rule")
@Excel("系统配置-编码规则")
public class SysconfCodeRule extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 规则编号
     */
    @TableField("rule_code")
    @ExcelField(value = "规则编号")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("rule_name")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 规则串
     */
    @TableField("rule_code_str")
    @ExcelField(value = "规则串")
    private String ruleCodeStr;

    /**
     * 流水初始值
     */
    @TableField("rule_seq_init")
    @ExcelField(value = "流水初始值")
    private Long ruleSeqInit;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 状态: enabled-有效;disabled-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: enabled-有效;disabled-无效")
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
