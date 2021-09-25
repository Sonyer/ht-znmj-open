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
 * 系统-资源组 Entity
 *
 * @author Eric
 * @date 2020-05-23 14:55:18
 */
@Data
@TableName("sys_group")
@Excel("系统-资源组")
public class SysGroup extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组编号
     */
    @TableField("group_code")
    @ExcelField(value = "组编号")
    private String groupCode;

    /**
     * 组名称
     */
    @TableField("group_name")
    @ExcelField(value = "组名称")
    private String groupName;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 状态
     */
    @TableField("status")
    @ExcelField(value = "状态")
    private String status;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
