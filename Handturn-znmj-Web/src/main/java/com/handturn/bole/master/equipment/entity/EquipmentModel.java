package com.handturn.bole.master.equipment.entity;

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
 * 基础资料-设备型号管理 Entity
 *
 * @author Eric
 * @date 2020-09-11 08:20:08
 */
@Data
@TableName("equipment_model")
@Excel("基础资料-设备型号管理")
public class EquipmentModel extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备型号编码
     */
    @TableField("equipment_model_code")
    @ExcelField(value = "设备型号编码")
    private String equipmentModelCode;

    /**
     * 设备型号名称
     */
    @TableField("equipment_model_name")
    @ExcelField(value = "设备型号名称")
    private String equipmentModelName;

    /**
     * 设备类型:FACE-人脸识别,FINGER-指纹识别,PASSWORD-密码识别,FP-人脸密码(二合一),FP-指纹密码(二合一)
     */
    @TableField("equipment_type")
    @ExcelField(value = "设备类型:FACE-人脸识别,FINGER-指纹识别,PASSWORD-密码识别,FP-人脸密码(二合一),FP-指纹密码(二合一)")
    private String equipmentType;

    /**
     * 厂商名称
     */
    @TableField("firm_name")
    @ExcelField(value = "厂商名称")
    private String firmName;

    /**
     * 状态: ENABLED-已启用;DISABLED-已禁用
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-已启用;DISABLED-已禁用")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
