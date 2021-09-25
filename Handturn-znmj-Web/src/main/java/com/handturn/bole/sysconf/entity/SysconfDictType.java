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
 * 系统-数据字典类型表 Entity
 *
 * @author MrBird
 * @date 2019-12-08 20:56:08
 */
@Data
@TableName("sysconf_dict_type")
@Excel("系统-数据字典类型表")
public class SysconfDictType extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据字典类型编码
     */
    @TableField("type_code")
    @ExcelField(value = "数据字典类型编码")
    private String typeCode;

    /**
     * 数据字典类型名称
     */
    @TableField("type_name")
    @ExcelField(value = "数据字典类型名称")
    private String typeName;

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
