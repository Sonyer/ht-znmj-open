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
 * 系统-系统配置参数 Entity
 *
 * @author Eric
 * @date 2019-12-18 20:31:06
 */
@Data
@TableName("sysconf_global_param")
@Excel("系统-系统配置参数")
public class SysconfGlobalParam extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组编码
     */
    @TableField("group_code")
    @ExcelField(value = "分组编码")
    private String groupCode;

    /**
     * 分组描述
     */
    @TableField("group_name")
    @ExcelField(value = "分组描述")
    private String groupName;

    /**
     * 参数键
     */
    @TableField("param_key")
    @ExcelField(value = "参数键")
    private String paramKey;

    /**
     * 参数值
     */
    @TableField("param_value")
    @ExcelField(value = "参数值")
    private String paramValue;

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
     * 网点编码
     */
    @TableField("oc_code")
    @ExcelField(value = "网点编码")
    private String ocCode;

    /**
     * 组织编号
     */
    @TableField("org_code")
    @ExcelField(value = "组织编号")
    private String orgCode;

    /**
     * 是否系统创建(1:是 0:否)，系统创建的记录不能前台修改和删除
     */
    @TableField("is_sys_created")
    @ExcelField(value = "是否系统创建(1:是 0:否)，系统创建的记录不能前台修改和删除")
    private String isSysCreated;

    //系统Id
    @TableField(exist = false)
    private Long sysParamId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
