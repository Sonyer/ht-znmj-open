package com.handturn.bole.websocket.bean.entity;

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
public class VisitorLogVo {
    private String id;

    private String visitorId;

    private String visitorCloudId;

    private String equipmentId;

    private String equipmentCloudId;

    private String visitorName;

    private String phoneNumber;

    private String idCard;

    private String department;

    private String positor;

    private String wigan;

    private String equipmentSn;

    private String areaName;

    private String seqNum;

    private String inOut;

    private Date openTime;

    private String openStatus;

    private String faceFilePath;

    private String cloudFlag;

    private String logFlag;

    private Date createTime;

    private Date updateTime;

}
