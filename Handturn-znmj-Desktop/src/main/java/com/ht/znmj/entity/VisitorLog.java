package com.ht.znmj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 人员信息
 */
@Data
@TableName("VisitorLog")
public class VisitorLog {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 人员ID
     */
    @TableField("visitorId")
    private String visitorId;

    @TableField("visitorCloudId")
    private String visitorCloudId;

    /**
     * 设备ID
     */
    @TableField("equipmentId")
    private String equipmentId;

    @TableField("equipmentCloudId")
    private String equipmentCloudId;

    @TableField("visitorName")
    private String visitorName;

    //手机号码
    @TableField("phoneNumber")
    private String phoneNumber;

    //身份证
    @TableField("idCard")
    private String idCard;

    //部门名称
    @TableField("department")
    private String department;

    //岗位名称
    @TableField("positor")
    private String positor;

    //韦根号
    @TableField("wigan")
    private String wigan;

    /**
     * 设备号
     */
    @TableField("equipmentSn")
    private String equipmentSn;

    //设备区域名称
    @TableField("areaName")
    private String areaName;

    //设备编号
    @TableField("seqNum")
    private String seqNum;

    //设备出入  0-未定义 1-入口  2-出口
    @TableField("inOut")
    private String inOut;

    //开闸时间
    @TableField("openTime")
    private Date openTime;

    //开闸状态
    @TableField("openStatus")
    private String openStatus;

    //人脸闸机截图地址
    @TableField("faceFilePath")
    private String faceFilePath;

    //云端同步  1-已同步  0-未同步
    @TableField("cloudFlag")
    private String cloudFlag;

    //日志类型：0-离线数据 1-在线数据
    @TableField("logFlag")
    private String logFlag;

    //新增时间
    @TableField("createTime")
    private Date createTime;

    //修改时间
    @TableField("updateTime")
    private Date updateTime;

    //开始时间
    @TableField(exist = false)
    private Long startOpenTime;
    //结束时间
    @TableField(exist = false)
    private Long endOpenTime;
}
