package com.handturn.bole.main.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * 基础资料-中间件管理 Entity
 *
 * @author Eric
 * @date 2020-12-02 11:13:52
 */
@Data
@TableName("middleware_info")
@Excel("基础资料-中间件管理")
public class MiddlewareInfo{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * MAC地址
     */
    @TableField("mac_id")
    @ExcelField(value = "MAC地址")
    private String macId;

    /**
     * 桌面应用版本
     */
    @TableField("app_version")
    @ExcelField(value = "桌面应用版本")
    private String appVersion;

    /**
     * 操作系统名称
     */
    @TableField("system_name")
    @ExcelField(value = "操作系统名称")
    private String systemName;

    /**
     * 操作系统版本
     */
    @TableField("system_version")
    @ExcelField(value = "操作系统版本")
    private String systemVersion;

    /**
     * 局域网IP
     */
    @TableField("lan_ip")
    @ExcelField(value = "局域网IP")
    private String lanIp;

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
     * 设备MAC地址
     */
    @TableField("equipment_mac_id")
    @ExcelField(value = "设备MAC地址")
    private String equipmentMacId;

    /**
     * 区域名称
     */
    @TableField("area_name")
    @ExcelField(value = "区域名称")
    private String areaName;

    /**
     * 设备序号
     */
    @TableField("seq_num")
    @ExcelField(value = "设备序号")
    private String seqNum;

    /**
     * 0-未定义;1-入口;2-出口
     */
    @TableField("in_out_flag")
    @ExcelField(value = "0-未定义;1-入口;2-出口")
    private String inOutFlag;

    /**
     * 上次心跳时间
     */
    @TableField("online_time")
    @ExcelField(value = "上次心跳时间")
    private Date onlineTime;

    /**
     * 状态: OFFLINE-离线;ONLINE-在线
     */
    @TableField("online_status")
    @ExcelField(value = "状态: OFFLINE-离线;ONLINE-在线")
    private String onlineStatus;

    /**
     * 版本编码
     */
    @TableField("record_version")
    @ExcelField(value = "版本编码")
    private Long recordVersion;

    /**
     * 新增时间
     */
    @TableField("create_date")
    @ExcelField(value = "新增时间")
    private Date createDate;

    /**
     * 最后修改时间
     */
    @TableField("last_upd_date")
    @ExcelField(value = "最后修改时间")
    private Date lastUpdDate;

}
