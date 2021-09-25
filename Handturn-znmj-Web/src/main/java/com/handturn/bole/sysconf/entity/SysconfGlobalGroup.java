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
 * 系统-系统配置 Entity
 *
 * @author Eric
 * @date 2019-12-18 20:25:27
 */
@Data
@TableName("sysconf_global_group")
@Excel("系统-系统配置")
public class SysconfGlobalGroup extends BasicEntity{

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


    //系统Id
    @TableField(exist = false)
    private Long sysGroupId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
