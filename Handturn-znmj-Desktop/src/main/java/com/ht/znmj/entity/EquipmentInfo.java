package com.ht.znmj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 设备信息
 */
@Data
@TableName("EquipmentInfo")
public class EquipmentInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("sn")
    private String sn;
    //设备系统版本号
    @TableField("version")
    private String version;
    //设备IP地址
    @TableField("ip")
    private String ip;
    //设备区域名称
    @TableField("areaName")
    private String areaName;
    //设备编号
    @TableField("seqNum")
    private String seqNum;
    //设备出入  0-未定义 1-入口  2-出口
    @TableField("inOut")
    private String inOut;
    //设备连接状态  1-有效   0-未连接
    @TableField("status")
    private String status;
    //设备连接状态  1-已连接   0-未连接
    @TableField("connStatus")
    private String connStatus;
    //云端同步  1-已同步  0-未同步
    @TableField("cloudFlag")
    private String cloudFlag;
    @TableField("cloudId")
    private String cloudId;
    //许可证书
    @TableField("license")
    private String license;
    //心跳时间
    @TableField("maxLogSeq")
    private Integer maxLogSeq;
    //心跳时间
    @TableField("heartbeatTime")
    private Date heartbeatTime;
    //新增时间
    @TableField("createTime")
    private Date createTime;
    //修改时间
    @TableField("updateTime")
    private Date updateTime;
}
