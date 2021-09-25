package com.handturn.bole.main.equipment.entity;

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
 * 基础资料-设备管理 Entity
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
@Data
@TableName("equipment_info")
@Excel("基础资料-设备管理")
public class EquipmentInfo extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备系统编码
     */
    @TableField("equipment_code")
    @ExcelField(value = "设备系统编码")
    private String equipmentCode;

    /**
     * 设备出厂编码
     */
    @TableField("equipment_md_code")
    @ExcelField(value = "设备出厂编码")
    private String equipmentMdCode;

    /**
     * 设备型号编号
     */
    @TableField("equipment_model_code")
    @ExcelField(value = "设备型号编号")
    private String equipmentModelCode;

    /**
     * 网点编码
     */
    @TableField("oc_code")
    @ExcelField(value = "网点编码")
    private String ocCode;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ExcelField(value = "组织编码")
    private String orgCode;

    /**
     * 网点名称
     */
    @TableField("oc_name")
    @ExcelField(value = "网点名称")
    private String ocName;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ExcelField(value = "组织名称")
    private String orgName;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 设备序号
     */
    @TableField("seq_num")
    @ExcelField(value = "设备序号")
    private String seqNum;

    /**
     * 进出标识:INDOOR-进门；OUTDOOR-出门；CLOCKIN-打卡
     */
    @TableField("in_out_flag")
    @ExcelField(value = "进出标识")
    private String inOutFlag;

    /**
     * 所属区域编码
     */
    @TableField("area_code")
    @ExcelField(value = "所属区域编码")
    private String areaCode;

    /**
     * 状态: UNERECT-未安装;ERECTED-已安装
     */
    @TableField("erect_status")
    @ExcelField(value = "状态: UNERECT-未安装;ERECTED-已安装")
    private String erectStatus;

    /**
     * 状态: OFFLINE-离线;ONLINE-在线
     */
    @TableField("online_status")
    @ExcelField(value = "状态: OFFLINE-离线;ONLINE-在线")
    private String onlineStatus;

    /**
     * 上次心跳时间
     */
    @TableField("online_time")
    @ExcelField(value = "上次心跳时间")
    private Date onlineTime;

    /**
     * 客户区域编码
     */
    @TableField(exist = false)
    private String areaClientCode;

    /**
     * 区域名称
     */
    @TableField(exist = false)
    private String areaName;

    //----------查询列表需要---------
    /**
     * 是否查询权限
     */
    @TableField(exist = false)
    private Boolean authCheck;
    /**
     * 查询权限区域ID
     */
    @TableField(exist = false)
    private Long authAreaId;

    /**
     * 查询权限区域ID
     */
    @TableField(exist = false)
    private String layChecked;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
