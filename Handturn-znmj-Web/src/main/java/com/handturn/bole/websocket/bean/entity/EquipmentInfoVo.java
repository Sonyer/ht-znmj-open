package com.handturn.bole.websocket.bean.entity;

import lombok.Data;

import java.util.Date;

/**
 * 设备信息
 */
@Data
public class EquipmentInfoVo {
    private String id;

    private String sn;
    //设备系统版本号
    private String version;
    //设备IP地址
    private String ip;
    //设备区域名称
    private String areaName;
    //设备编号
    private String seqNum;
    //设备出入  0-未定义 1-入口  2-出口
    private String inOut;
    //设备连接状态  1-有效   0-未连接
    private String status;
    //设备连接状态  1-已连接   0-未连接
    private String connStatus;
    //云端同步  1-已同步  0-未同步
    private String cloudFlag;
    //许可证书
    private String license;
    //心跳时间
    private Date heartbeatTime;
    //新增时间
    private Date createTime;
    //修改时间
    private Date updateTime;

    private String cloudId;
}
